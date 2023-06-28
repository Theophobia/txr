import React, {useState, useEffect} from "react";
import {useParams} from "react-router-dom";
import axios from "axios";
import useAuth from "../useAuth";
import "./Chat.css";

interface AnonymousMessage {
	senderUsername: string,
	timestamp: string,
	type: string,
	data: string,
	bonusData: string | null
}

function Chat() {
	const {username} = useParams();
	const auth = useAuth();
	const [messages, setMessages] = useState<AnonymousMessage[]>([]);
	const [currMsg, setCurrMsg] = useState("");

	useEffect(() => {
		if (auth.isLoggedIn) {
			fetchMessages(username).then(() => {});
		}
	}, [username, auth.isLoggedIn, auth.isLoading]);

	const fetchMessages = async (username) => {
		try {
			if (auth.userInfo === null || auth.authToken === null) {
				return;
			}

			const data = {
				userId: auth.userInfo.userId,
				token: auth.authToken,
				receiver: username,
				pageNumber: 0,
				pageSize: 10
			};

			const response = await axios.get("http://localhost:8080/api/message/latest", {params: data});
			setMessages(response.data.map((message) => {
				message.data = decodeURIComponent(message.data);
				return message;
			}));
			console.log(response.data);
		} catch (error) {
			console.error("Error fetching messages:", error);
		}
	};

	const handleEnter = async (event) => {
		if (event.key === "Enter") {
			const trimmed = currMsg.trim();
			if (trimmed.length === 0) {
				return;
			}

			console.log(trimmed);

			 if (auth.userInfo === null || auth.authToken === null) {
				 return;
			 }

			const data = {
				userId: encodeURIComponent(auth.userInfo.userId),
				token: encodeURIComponent(auth.authToken),
				receiver: encodeURIComponent(username),
				message: encodeURIComponent(trimmed)
			};
			/**
			 * 		@RequestParam long userId,
			 * 		@RequestParam String token,
			 * 		@RequestParam String receiver,
			 * 		@RequestParam String message
			 */
			const response = await axios.post("http://localhost:8080/api/message/send", {}, {params: data});
			await fetchMessages(username);

			event.target.value = "";
			setCurrMsg("");
		}
	};

	return (
		<div className={"chat"}>
			<div className={"chat_messages"}>
				{auth.isLoading ? <div/> : messages && messages.length > 0 && messages.map((message) => (
					<div key={message.timestamp} className={true ? "chat__message" : "chat__message_other"}>
						{message.data}
					</div>
				))}
			</div>
			<div className={"chat_input_div"}>
				<input
					className={"chat_input"}
					onKeyDown={handleEnter}
					onChange={e => setCurrMsg(e.target.value)}
				>

				</input>
			</div>
		</div>
	);
}

export default Chat;
