import { useParams, useNavigate } from 'react-router-dom';
import Sidebar from "./Sidebar";
import {useEffect, useState} from "react";
import Message from "../api/message";
import "./Chat.css";
import UserData from "../api/userData";
import {loginSuccess, logout} from "../state/authActions";
import {sidebarShown} from "../state/visibilityActions";
import {AuthState} from "../state/authState";
import {useDispatch, useSelector} from "react-redux";
import apiChatMessageGet = Query.apiChatMessageGet;

const Chat = () => {
	const {username} = useParams();
	const [messages, setMessages] = useState<Message[]>([]);

	const dispatch = useDispatch();
	const navigate = useNavigate();

	const auth: AuthState = useSelector((state) => state.auth);

	const fetchMessages = async () => {

		if (auth.userData === null) {
			console.log("User data is null, returning");
			return;
		}

		if (auth.token === null) {
			console.log("Token is null, returning");
			return;
		}

		try {
			// Send authentication request
			const response = await apiChatMessageGet(auth.userData.userId, auth.token, auth.userData.username, 0, 20);

			if (response === null) {
				throw new Error();
			}

			if (response.status === 401) {
				// console.log("Expired auth token, logging out");
				dispatch(logout());
				// TODO send query to server for logging out
				return;
			}

			if (!response.ok) {
				return;
			}

			const data: Message[] = await response.json();
			setMessages(data);
			console.log(data);
		}
		catch (error) {
			// Handle authentication error
			// dispatch(loginFailure(error.message));
		}
	};

	useEffect(() => {
		if (!auth.isLoggedIn) {
			navigate("/");
			return;
		}
		fetchMessages();
	}, [username]);

	return (
		<>{!auth.isLoggedIn ? <></> :
			<div className={"chat_root"}>
				<div className={"chat_header"}>{username}</div>
				<div className={"chat_container"}>
					{messages.map(m =>
						<div key={m.timestamp.concat(m.senderUsername)}
							 className={m.senderUsername === username ? "msg_outer_box_me" : "msg_outer_box_other"}
						>
							<div className={m.senderUsername === username ? "msg_any msg_me" : "msg_any msg_other"}>
								{m.data}
							</div>
						</div>
					)}
				</div>
			</div>
		}</>
	);
};

export default Chat;
