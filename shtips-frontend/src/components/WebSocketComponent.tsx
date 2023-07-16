import React, { useEffect, useState } from "react";
import {AuthState} from "../state/authState";
import {useSelector} from "react-redux";
import {Update, UpdateType} from "../updateType";

const WebSocketComponent: React.FC = (props: {onNewMessage: (sender: string, receiver: string) => {}}) => {
	const [socket, setSocket] = useState<WebSocket | null>(null);

	const auth: AuthState = useSelector((state) => state.auth);

	useEffect(() => {
		// Create and open the WebSocket connection when the component mounts
		const newSocket = new WebSocket("ws://localhost:8080/api/ws");
		setSocket(newSocket);

		// Clean up the WebSocket connection when the component unmounts
		return () => {
			newSocket.close();
		};
	}, [props /* TODO: new connection on every chat change*/]);

	useEffect(() => {
		if (socket) {
			// Event handler for WebSocket connection open
			socket.onopen = () => {
				// Send the authentication token to the server
				if (auth.token) {
					socket.send("token=".concat(auth.token));
					console.log("Sent auth token through WebSocket.");
				}
				else {
					console.error("Auth state is not complete, could not send auth token.");
				}
			};

			// Event handler for receiving messages from the server
			socket.onmessage = (event) => {
				try {
					// Handle the received data as needed
					const receivedData: string = event.data;
					console.log("Received data: ", receivedData);

					const json = JSON.parse(receivedData);
					console.log("Json: ", json);

					if (!("type" in json)) {
						console.error("Received message does not have field 'type'");
						return;
					}

					switch (json.type) {
						case "NEW_MESSAGE": {
							if (!("sender" in json)) {
								console.error("Incorrect message, does not have 'sender' field");
								return;
							}

							if (!("receiver" in json)) {
								console.error("Incorrect message, does not have 'receiver' field");
								return;
							}

							console.log("Firing onNewMessage()");
							props.onNewMessage(json.sender, json.receiver);
							break;
						}
						default: {
							console.log("Default");
							break;
						}
					}
				}
				catch (e) {
					console.error("Error parsing received message: ", e);
				}
			};

			// Event handler for WebSocket connection errors
			socket.onerror = (error) => {
				console.error("WebSocket error: ", error);
			};

			// Event handler for WebSocket connection close
			socket.onclose = (event) => {
				console.log("WebSocket connection closed: ", event);
			};
		}
	}, [socket]);

	return (
		<></>
	);
};

export default WebSocketComponent;
