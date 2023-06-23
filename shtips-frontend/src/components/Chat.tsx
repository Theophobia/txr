import React, {useState, useEffect} from "react";
import {useParams} from "react-router-dom";
import axios from "axios";
import "./Chat.css";

function Chat() {
	const {userId} = useParams();
	const [messages, setMessages] = useState([]);

	useEffect(() => {
		fetchMessages(userId);
	}, [userId]);

	const fetchMessages = async (userId) => {
		try {
			const response = await axios.get(`/api/messages/${userId}`);
			setMessages(response.data);
		} catch (error) {
			console.error("Error fetching messages:", error);
		}
	};

	return (
		<div className="chat">
			{messages.map((message) => (
				<div key={message.id} className="chat__message">
					{/* Render message content and timestamp */}
				</div>
			))}
		</div>
	);
}

export default Chat;
