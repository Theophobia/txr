import { useParams, useNavigate } from 'react-router-dom';
import {useEffect, useState, Suspense, useReducer, useRef} from "react";
import Message from "../api/message";
import "./Chat.css";
import {logout} from "../state/authActions";
import {AuthState} from "../state/authState";
import {useDispatch, useSelector} from "react-redux";
import {apiChatMessageGet, apiChatMessageSend} from "../query";

const Chat = () => {
	const {username} = useParams();
	const [messages, setMessages] = useState<Message[]>([]);
	const [message, setMessage] = useState("");
	const [shouldRefetch, setShouldRefetch] = useState(false);
	const [showChatContainer, setShowChatContainer] = useState(false);
	const messagesEndRef = useRef<HTMLDivElement | null>(null);
	const [shouldRedirectToHomePage, setShouldRedirectToHomePage] = useState(false);

	const dispatch = useDispatch();
	const navigate = useNavigate();

	const auth: AuthState = useSelector((state) => state.auth);

	const fetchMessages = async (deleteMessages: boolean) => {
		setShowChatContainer(false);

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
			const response = await apiChatMessageGet(auth.userData.userId, auth.token, username, 0, 20);

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
			const messagesCopy = deleteMessages ? [] : messages;

			data.forEach((m) => {
				if (!messagesCopy.find((value) => value.timestamp === m.timestamp && value.senderUsername === m.senderUsername)) {
					messagesCopy.push(m);
				}
			});
			messagesCopy.sort((a, b) => {
				return a.timestamp.concat(a.senderUsername).localeCompare(b.timestamp.concat(b.senderUsername))
			});

			setMessages(messagesCopy);
			setShowChatContainer(true);
			// console.log(data);
		}
		catch (error) {
			// Handle authentication error
			// dispatch(loginFailure(error.message));
		}
	};

	const sendMessage = async (event) => {
		if (event.key !== "Enter") {
			return;
		}

		if (message.length === 0) {
			return;
		}

		if (auth.userData === null || auth.token === null) {
			console.log("Might be an error, and might need to log out.");
			return;
		}

		const response = await apiChatMessageSend(auth.userData.userId, auth.token, username, message);
		setShouldRefetch(true);

		setMessage("");
		event.target.value = "";
	}

	const scrollToBottom = () => {
		// noinspection TypeScriptValidateTypes
		messagesEndRef.current?.scrollIntoView();
	}

	useEffect(() => {
		if (!auth.isLoggedIn) {
			navigate("/");
			return;
		}
		fetchMessages(true).then(scrollToBottom);
	}, [username]);

	useEffect(() => {
		if (!auth.isLoggedIn) {
			navigate("/");
			return;
		}
		if (shouldRefetch) {
			setShouldRefetch(false);
			fetchMessages(false);
		}
	}, [shouldRefetch]);

	useEffect(() => {
		if (shouldRedirectToHomePage) {
			setShouldRedirectToHomePage(false);
			navigate("/");
		}
	}, [shouldRedirectToHomePage]);

	return (
		<>{!auth.isLoggedIn
			?
			<>
				<div>You are not logged in! You should return to the login page.</div>
				{() => setShouldRedirectToHomePage(true)}
			</>
			:
			<div className={"chat_root"}>
				<div className={"chat_header"}>{username}</div>
				{showChatContainer && <>
					<div className={"chat_container"}>
						{messages.length !== 0 && messages.map((m) =>
							<div key={m.timestamp.concat(m.senderUsername)}
								 className={m.senderUsername === username ? "msg_outer_box_other" : "msg_outer_box_me"}
							>
								{m.senderUsername === username ?
									<img className={"msg_img_other"} src={`http://localhost:8080/api/test/getAvatar?username=${username}`}></img>
									:
									<img className={"msg_img_me"} src={`http://localhost:8080/api/test/getAvatar?username=${auth.userData?.username}`}></img>
								}
								<div className={m.senderUsername === username ? "msg_any msg_other" : "msg_any msg_me"}>
									{m.data}
								</div>
							</div>
						)}
						<div ref={messagesEndRef}></div>
					</div>
					<div className={"chat_input_box"}>
					<textarea className={"chat_input"}
							  onKeyDown={(event) => sendMessage(event)}
							  onChange={(event) => setMessage(event.target.value)}

					/>
					</div>
				</>}
			</div>
		}</>
	);
};

export default Chat;
