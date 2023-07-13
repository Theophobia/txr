import React, {useEffect, useState} from "react";
import {useSelector} from "react-redux";
import {AuthState} from "../state/authState";

const RealtimeUpdate: React.FC = (props: {url: string}) => {
	const auth: AuthState = useSelector((state) => state.auth);

	const [socket, setSocket] = useState<WebSocket | null>(null);


	// Send a message
	function sendMessage(message) {
		socket?.send(message);
	}

	// Close the WebSocket connection
	function closeConnection() {
		socket?.close();
	}

	useEffect(() => {
		const newSocket = new WebSocket("ws://localhost:8080".concat(props.url));

		// Connection open event
		newSocket.onopen = () => {
			console.log('WebSocket connection established.');
			setSocket(newSocket);

			if (auth.token === null) {
				return;
			}
			sendMessage(JSON.stringify({token: "/token/".concat(auth.token)}));
		};

		// Message received event
		newSocket.onmessage = (event) => {
			const receivedMessage = event.data;
			console.log("Received WebSocket message:", receivedMessage);
		};

		// WebSocket Error
		newSocket.onerror = (event) => {
			console.error("WebSocket connection error:", event);
		}

		setSocket(newSocket);

		return () => {
			if (newSocket) {
				newSocket.close();
				setSocket(null);
			}
		};
	}, [props.url]);

	return <></>;
};

export default RealtimeUpdate;
