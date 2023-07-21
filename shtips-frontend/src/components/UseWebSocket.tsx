import React, {useEffect, useState} from "react";
import {AuthState} from "../api/authState";
import {useSelector} from "react-redux";
import Message, {MessageStatus} from "../api/message";
import {Event1, Event10, Event12, Event14} from "../api/event";

const useWebSocket = (props: {
	onNewMessage: (msg: Message) => void,
	onMessageConfirm: (event12: Event12) => void,
	onMessageFetched: (event14: Event14) => void,
}) => {
	const [socket, setSocket] = useState<WebSocket | null>(null);

	const auth: AuthState = useSelector((state) => state.auth);

	useEffect(() => {
		const newSocket = new WebSocket("ws://localhost:8080/api/ws");

		// Event handler for WebSocket connection open
		newSocket.onopen = () => {
			// Send the authentication token to the server
			if (auth.token && auth.userData) {
				const event1: Event1 = {
					token: auth.token,
					userId: auth.userData.userId,
				}

				newSocket.send("0001" + JSON.stringify(event1));
				console.log("Sent event1 through WebSocket.");
			}
			else {
				console.error("Auth state is not complete, could not send auth token.");
			}
		};

		// Event handler for receiving messages from the server
		newSocket.onmessage = (event) => {
			try {
				// Handle the received data as needed
				const eventData: string = event.data;
				console.info("Received data:", eventData);

				const idString = eventData.substring(0, 4);
				console.info("Found idString:", idString);

				const id = parseInt(idString);
				console.info("Parsed id:", id);

				const data = eventData.substring(4);
				console.info("Found data:", data);

				switch (id) {
					case 10: {
						const event10: Event10 = JSON.parse(data);
						console.info("Parsed event10:", event10);

						const msg: Message = {
							messageId: event10.messageId,
							status: MessageStatus.DELIVERED,
							sender: event10.sender,
							timestamp: event10.timestamp,
							type: event10.type,
							data: event10.data,
							bonusData: event10.bonusData,
						}

						props.onNewMessage(msg);
						break;
					}

					case 12: {
						const event12: Event12 = JSON.parse(data);
						console.info("Parsed event12:", event12);

						props.onMessageConfirm(event12);
						break;
					}

					case 14: {
						const event14: Event14 = JSON.parse(data);
						console.info("Parsed event14:", event14);

						props.onMessageFetched(event14);
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
		newSocket.onerror = (error) => {
			console.error("WebSocket error: ", error);
		};

		// Event handler for WebSocket connection close
		newSocket.onclose = (event) => {
			console.log("WebSocket connection closed: ", event);
		};

		setSocket(newSocket);

		return () => {
			newSocket.close();
		}
	}, []);

	const send = (message: string) => {
		if (socket && socket.readyState === WebSocket.OPEN) {
			socket.send(message);
		}
	}

	return {send};
};

export default useWebSocket;
