import React, { useEffect, useState } from "react";
import {AuthState} from "./state/authState";
import {useSelector} from "react-redux";
import {Update, UpdateType} from "./updateType";

const WebSocketComponent: React.FC = (props: {onNewMessage: () => {any}}) => {
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
	}, []);

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
				const receivedData: string = event.data;
				// Handle the received data as needed
				console.log("Received data: ", receivedData);

				switch (receivedData) {
					case "NEW_MESSAGE":
						console.log("Firing onNewMessage()");
						props.onNewMessage();
						break;

					default:
						console.log("Default");
						break;
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
