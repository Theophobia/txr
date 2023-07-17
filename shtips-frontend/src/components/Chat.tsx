import { useParams, useNavigate } from 'react-router-dom';
import React, {useEffect, useState, useRef} from "react";
import Message from "../api/message";
import "./Chat.css";
import {logout} from "../state/authActions";
import {AuthState} from "../state/authState";
import {useDispatch, useSelector} from "react-redux";
import {apiChatMessageGet, apiChatMessageSend} from "../util/query";
import useWebSocket from "./UseWebSocket";
import {Event11, Event12} from "../api/event";

const Chat = () => {
	const {username} = useParams();
	const [messages, setMessages] = useState<Message[]>([]);
	const [message, setMessage] = useState("");

	const [shouldRefetch, setShouldRefetch] = useState(false);
	const [shouldFetchOlderMessages, setShouldFetchOlderMessages] = useState(true);

	const [send] = useWebSocket({
		onNewMessage: (msg: Message) => {
			if (msg.sender === username) {
				console.log("Appending message, as sender === username, ", msg.sender, username);
				setMessages((prev) => [...prev, msg]);
			}
			else {
				console.info("onNewMessage, but sender !== username")
			}
		},
		onMessageConfirm: (event12: Event12) => {
			console.log("Event12", event12);
		}
	});

	const [showChatContainer, setShowChatContainer] = useState(true);

	const [currPageIndex, setCurrPageIndex] = useState<number>(0);

	const dispatch = useDispatch();
	const navigate = useNavigate();
	const messagesEndRef = useRef<HTMLDivElement | null>(null);

	const auth: AuthState = useSelector((state) => state.auth);

	const fetchMessages = async (pageNumber: number, deleteMessages: boolean, callbacks?: {callbackData?: ((data: Message[]) => any), callbackFinish?: (() => Promise<any>)}) => {
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
			const response = await apiChatMessageGet(auth.userData.userId, auth.token, username, pageNumber, 8);

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

			if (callbacks?.callbackData) {
				await callbacks.callbackData(data);
			}

			data.forEach((m) => {
				if (!messagesCopy.find((value) => value.timestamp === m.timestamp && value.sender === m.sender)) {
					messagesCopy.push(m);
				}
			});
			messagesCopy.sort((a, b) => {
				return a.timestamp.concat(a.sender).localeCompare(b.timestamp.concat(b.sender))
			});

			setMessages(messagesCopy);
			setShowChatContainer(true);
			// console.log(data);

			if (callbacks?.callbackFinish) {
				await callbacks.callbackFinish();
			}
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

		const filteredMessage = message
			.replace(/[\u0000-\u001F\u007F-\u009F]/g, "")
			.trim();

		if (filteredMessage.length === 0) {
			return;
		}

		if (auth.userData === null || auth.token === null) {
			console.log("Might be an error, and might need to log out.");
			return;
		}
		const now = new Date();

		const year = now.getFullYear();
		const month = (now.getMonth() + 1).toString().padStart(2, '0'); // Months are zero-based
		const day = now.getDate().toString().padStart(2, '0');
		const hours = now.getHours().toString().padStart(2, '0');
		const minutes = now.getMinutes().toString().padStart(2, '0');
		const seconds = now.getSeconds().toString().padStart(2, '0');
		const milliseconds = now.getMilliseconds().toString().padStart(3, '0');

		const currentDateTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}.${milliseconds}`;

		const event11: Event11 = {
			userId: auth.userData.userId,
			token: auth.token,
			receiver: username,
			timestamp: currentDateTime,
			type: "TEXT",
			data: filteredMessage,
			bonusData: null,
		};
		console.log("event11 = ", event11);

		send("0011" + JSON.stringify(event11));

		const msg: Message = {
			sender: auth.userData.username,
			timestamp: event11.timestamp,
			type: event11.type,
			data: event11.data,
			bonusData: event11.bonusData,
		}

		setMessages(prevState => [...prevState, msg]);
		setMessage("");
		event.target.value = "";
	}

	const timeout = (delay: number) => {
		return new Promise(res => setTimeout(res, delay));
	}

	const scrollToBottom = async () => {
		await timeout(300);
		// noinspection TypeScriptValidateTypes
		messagesEndRef.current?.scrollIntoView({behavior: "smooth"});
		console.log("Scrolling to bottom");
	}

	const handleScroll = (event: React.UIEvent<HTMLDivElement>) => {
		const scrollTop = event.currentTarget.scrollTop;
		// console.log(scrollTop);

		if (scrollTop === 0 && shouldFetchOlderMessages) {
			fetchMessages(currPageIndex + 1, false, {callbackData: data => {
				if (data.length !== 0) {
					setCurrPageIndex(currPageIndex + 1);
				}
				else {
					setShouldFetchOlderMessages(false);
				}
			}});
			console.log(scrollTop);
			console.log("a = " + event.currentTarget.scrollHeight);
		}
	};

	// Initial
	useEffect(() => {
		if (!auth.isLoggedIn) {
			navigate("/");
			return;
		}
		console.log("Username changed to \'", username, '\'');

		setCurrPageIndex(0);

		fetchMessages(0, true, {callbackFinish: scrollToBottom});
	}, [username]);

	// Fetching after sending a message
	useEffect(() => {
		if (!auth.isLoggedIn) {
			navigate("/");
			return;
		}
		if (shouldRefetch) {
			setShouldRefetch(false);
			fetchMessages(0, false, {callbackFinish: scrollToBottom});
		}
	}, [shouldRefetch]);

	// Redirect useEffect
	useEffect(() => {
		if (!auth.isLoggedIn) {
			navigate("/");
			return;
		}
	});

	// Fetch when scrolling up
	// useEffect(() => {
	// 	if (!auth.isLoggedIn) {
	// 		navigate("/");
	// 		return;
	// 	}
	// 	fetchMessages(currPageIndex, false);
	// }, [currPageIndex]);

	return (
		<>{!auth.isLoggedIn
			?
			<>
				<div>You are not logged in! You should return to the login page.</div>
			</>
			:
			<>
				<div className={"chat_root"}>
					<div className={"chat_header"}>{username}</div>
					{showChatContainer && <>
						<div className={"chat_container"}
							 onScroll={(event) => handleScroll(event)}>
							<div className={"chat_fodder"}/>
							{messages.length !== 0 && messages.map((m) =>
								<div key={m.timestamp.concat(m.sender)}
									 className={m.sender === username ? "msg_outer_box_other" : "msg_outer_box_me"}
								>
									{m.sender === username ?
										<img className={"msg_img_other"} src={`http://localhost:8080/api/test/getAvatar?username=${username}`}></img>
										:
										<img className={"msg_img_me"} src={`http://localhost:8080/api/test/getAvatar?username=${auth.userData?.username}`}></img>
									}
									<div className={m.sender === username ? "msg_any msg_other" : "msg_any msg_me"}>
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
			</>
		}</>
	);
};

export default Chat;
