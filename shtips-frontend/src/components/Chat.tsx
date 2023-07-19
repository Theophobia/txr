import {useNavigate, useParams} from "react-router-dom";
import React, {useEffect, useRef, useState} from "react";
import Message, {MessageStatus} from "../api/message";
import "./Chat.css";
import {logout} from "../state/authActions";
import {AuthState} from "../state/authState";
import {useDispatch, useSelector} from "react-redux";
import {apiChatMessageGet} from "../util/query";
import useWebSocket from "./UseWebSocket";
import {Event11, Event12, Event13, Event14} from "../api/event";

const Chat = () => {
	const {username} = useParams();
	const usernameRef = useRef<string>(username);
	const [messages, setMessages] = useState<Message[]>([]);
	const [message, setMessage] = useState("");

	const [shouldFetchOlderMessages, setShouldFetchOlderMessages] = useState(true);

	const {send} = useWebSocket({
		onNewMessage: (msg: Message) => {
			if (msg) {
				if (msg.sender === usernameRef.current) {
					console.log("Appending message, as sender === username, ", msg.sender, username);
					setMessages((prev) => [...prev, msg]);
					scrollToBottom();
				}
				else {
					console.info("onNewMessage, but sender !== username")
				}
			}
		},
		onMessageConfirm: (event12: Event12) => {
			if (event12) {
				console.log("Event12", event12);

				if (auth.userData === null) {
					console.error("This should not happen");
					return;
				}

				const msg: Message = {
					messageId: event12.messageId,
					status: MessageStatus.SENT,
					sender: auth.userData.username,
					timestamp: event12.timestamp,
					type: event12.type,
					data: event12.data,
					bonusData: event12.bonusData,
				}

				setMessages(prevState => [...prevState, msg]);
			}
		},
		onMessageFetched: (event14: Event14) => {
			if (event14) {
				if (event14.sender === usernameRef.current) {
					if (event14.messages.length === 0) {
						setShouldFetchOlderMessages(false);
					}
					else {
						setMessages(prevState => [...event14.messages, ...prevState]);
					}
				}
				else {
					console.error("Fetching messages for user, that is not in the current screen");
					return;
				}
			}
		}
	});

	const [showChatContainer, setShowChatContainer] = useState(true);

	const [currPageIndex, setCurrPageIndex] = useState<number>(0);

	const dispatch = useDispatch();
	const navigate = useNavigate();
	const messagesEndRef = useRef<HTMLDivElement | null>(null);

	const auth: AuthState = useSelector((state) => state.auth);

	const fetchMessages = async (before: string, deleteMessages: boolean, callbacks?: {callbackData?: ((data: Message[]) => any), callbackFinish?: (() => Promise<any>)}) => {
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
			const response = await apiChatMessageGet(auth.userData.userId, auth.token, username, before);

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
				if (!messagesCopy.find((value) => value.messageId === m.messageId)) {
					messagesCopy.push(m);
				}
			});
			messagesCopy.sort((a, b) => {
				return a.messageId > b.messageId ? 1 : -1;
			});

			setMessages(messagesCopy);
			setShowChatContainer(true);
			console.log(data);

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
			setMessage("");
			event.target.value = "";
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

		setMessage("");
		event.target.value = "";

		scrollToBottom();
	}

	const timeout = (delay: number) => {
		return new Promise(res => setTimeout(res, delay));
	}

	const scrollToBottom = async () => {
		await timeout(150);
		// noinspection TypeScriptValidateTypes
		messagesEndRef.current?.scrollIntoView({behavior: "smooth"});
		console.log("Scrolling to bottom");
	}

	const handleScroll = (event: React.UIEvent<HTMLDivElement>) => {
		const scrollTop = event.currentTarget.scrollTop;

		if (scrollTop === 0 && shouldFetchOlderMessages) {
			sendEvent13();
			console.log(scrollTop);
			console.log("a = " + event.currentTarget.scrollHeight);
		}
	};

	const sendEvent13 = () => {
		const before = (messages.length === 0 ? "" : messages[0].timestamp);

		if (auth.userData === null || auth.token === null) {
			console.log("Might be an error, and might need to log out.");
			return;
		}

		const event13: Event13 = {
			userId: auth.userData.userId,
			token: auth.token,
			receiver: username,
			timestamp: before,
		};
		console.log("event13 = ", event13);
		send("0013" + JSON.stringify(event13));
	};

	// Initial
	useEffect(() => {
		if (!auth.isLoggedIn) {
			navigate("/");
			return;
		}
		console.log("Username changed to \'", username, '\'');
		usernameRef.current = username;

		setCurrPageIndex(0);
		setShouldFetchOlderMessages(true);

		fetchMessages("", true, {callbackFinish: scrollToBottom});

	}, [username]);

	// Redirect useEffect
	useEffect(() => {
		if (!auth.isLoggedIn) {
			navigate("/");
			return;
		}
	});

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
							 onScroll={(event) => handleScroll(event)}
						>
							<div className={"chat_fodder"}/>
							<div className={"chat_get_more_messages_container"}>
								<div className={"chat_get_more_messages_button"}
									 onClick={() => sendEvent13()}
								>
									Get more messages
								</div>
							</div>
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
