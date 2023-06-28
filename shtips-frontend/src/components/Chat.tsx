import React, {useState, useEffect} from "react";
import {useParams} from "react-router-dom";
import axios from "axios";
import "./Chat.css";
import useAuth from "../useAuth";


function Chat() {
	const {username} = useParams();
	const {token, isTokenReady} = useAuth();
	const [messages, setMessages] = useState([]);

	useEffect(() => {
		if (isTokenReady) {
			fetchMessages(username);
		}
	}, [username, isTokenReady]);

	const fetchMessages = async (username) => {
		try {
			const data = {
				userId: 1,
				token: token,
				receiver: username,
				pageNumber: 0,
				pageSize: 3
			};
			const response = await axios.get("http://localhost:8080/api/message/latest", {params: data});
			setMessages(response.data);
			// console.log(response.data);
		} catch (error) {
			console.error("Error fetching messages:", error);
		}
	};

	return (
		<div className="chat">
			{messages.map((message) => (
				<div key={message.id} className={true ? "chat__message" : "chat__message_other"}>
					{message.data}
				</div>
			))}
		</div>
	);
}

export default Chat;
