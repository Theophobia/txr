import {AuthState} from "../api/authState";
import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import "./Sidebar.css"

import RecentChat from "../api/recentChat";
import {logout} from "../state/authActions";
import {apiChatRecent} from "../util/query";
import AvatarComponent from "./AvatarComponent";
import useWebSocket from "./UseWebSocket";
import Message, {MessageStatus} from "../api/message";
import {Event12, Event14, Event30, Event31, Event31Data} from "../api/event";
import useActivity from "./useActivity";

const Sidebar = () => {
	const [recentChats, setRecentChats] = useState<RecentChat[]>([]);

	const dispatch = useDispatch();
	const navigate = useNavigate();

	const auth: AuthState = useSelector((state) => state.auth);
	const show: boolean = useSelector((state) => state.visibility.isSidebarVisible);
	const activity = useActivity();

	const {send} = useWebSocket({
		slot: 0,
		channels: ["0010", "0012", "0020", "0031"],
		onActivityUpdate: (event31: Event31) => {
			const len = event31.data.length;
			for (let i = 0; i < len; i++) {
				const curr: Event31Data = event31.data[i];
				activity.setActivity(curr.username, curr.activity);
			}
		},
	});

	useEffect(() => {
		getRecentChats();
	}, [auth]);

	useEffect(() => {
		if (recentChats && recentChats.length != 0 && auth && auth.userData) {
			const usernames = recentChats.map(value => value.other_person_username);
			usernames.push(auth.userData.username);

			const event30: Event30 = {
				userId: auth.userData.userId,
				usernames: usernames,
			};
			send("0030" + JSON.stringify(event30));
		}
	}, [recentChats])

	async function getRecentChats() {
		try {
			if (auth.userData === null || auth.token === null) {
				return;
			}

			// Send authentication request
			const authResponse = await apiChatRecent(auth.userData.userId, auth.token);

			if (authResponse === null) {
				throw new Error();
			}

			if (authResponse.status === 401) {
				// console.log("Should logout, invalid auth token");
				dispatch(logout());
				// TODO: send api query
				return;
			}

			if (!authResponse.ok) {
				return;
			}

			// Extract the token from the response
			const data = await authResponse.json();
			console.log(data)

			setRecentChats(data);
		}
		catch (error) {
			// Handle authentication error
			// dispatch(loginFailure(error.message));
		}
	}

	return (
		<>
			{!auth.isLoggedIn || !show || auth.userData === null ?
				<>
				</>
				:
				<>
					<div className={"sidebar"}>
						<div className={"search_root"}>
							<AvatarComponent username={auth.userData.username}/>
							<div style={{paddingBottom: "10px"}}/>
							<div className={"search_input"}
								 onClick={() => navigate("/search")}
							>
								Search
							</div>
						</div>
						<div className={"sidebar_chats"}>
							{recentChats.map((chat) =>
								<div key={chat.other_person_username}
									 className={"sidebar_chat_container"}
									 onClick={() => navigate("/chat/".concat(chat.other_person_username))}
								>
									<AvatarComponent username={chat.other_person_username}/>
									<div className={"chat_username_container flex_centered"}>
										{chat.other_person_username}
									</div>
								</div>
							)}
						</div>
					</div>
				</>
			}
		</>
	);
};

export default Sidebar;
