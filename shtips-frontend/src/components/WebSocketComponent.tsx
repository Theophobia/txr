import React, { useEffect, useState } from "react";
import {AuthState} from "../state/authState";
import {useSelector} from "react-redux";
import {Update, UpdateType} from "../updateType";
import Message from "../api/message";

const WebSocketComponent: React.FC = (props: {onNewMessage: (msg: Message) => {}}) => {
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
					const eventData: string = event.data;
					console.info("Received data:", eventData);

					const idString = eventData.substring(0, 4);
					console.info("Found idString:", idString);

					const id = parseInt(idString);
					console.info("Found id:", id);

					const data = eventData.substring(4);
					console.info("Found data:", data);

					switch (id) {
						case 1: {
							const msg: Message = JSON.parse(data);
							console.info("Parsed message:", msg);

							props.onNewMessage(msg);
							break;
						}
						default: {
							console.error("Could not match ID, do we have different API versions?");
							break;
						}
					}
				}
				catch (e) {
					console.error("Error parsing received message:", e);
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
