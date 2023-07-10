import {AuthState} from "../state/authState";
import {useDispatch, useSelector} from "react-redux";
import { useNavigate } from "react-router-dom";
import {ChangeEvent, useEffect, useState} from "react";
import "./Sidebar.css"

import RecentChat from "../api/recentChat";
import {logout} from "../state/authActions";
import {apiChatRecent} from "../query";

const Sidebar = () => {
	const [recentChats, setRecentChats] = useState<RecentChat[]>([]);
	const [searchedUser, setSearchedUser] = useState<null | string>(null);

	const dispatch = useDispatch();
	const navigate = useNavigate();

	const auth: AuthState = useSelector((state) => state.auth);
	const isVisible: boolean = useSelector((state) => state.visibility.isSidebarVisible);

	useEffect(() => {
		getRecentChats()
	}, [auth]);

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

	function updateSearchedUser(event: ChangeEvent<HTMLInputElement>) {
		const value = event.target.value;
		setSearchedUser(value.length === 0 ? null : value);
	}

	async function searchForUser() {

	}

	return (
		<div className={"sidebar_root"}>
			{!auth.isLoggedIn || !isVisible ?
				<>
				</>
				:
				<>
					<div className={"search_root"}>
						<input onChange={(event) => updateSearchedUser(event)}
							   onClick={() => searchForUser()}
							   placeholder={"Search"}
							   className={"search_input"}
						/>
					</div>
					<div className={"sidebar"}>
						<div className={"sidebar_chats"}>
							{recentChats.map(chat =>
								<div key={chat.other_person_username}
									 className={"sidebar_user"}
									 onClick={() => navigate("/chat/".concat(chat.other_person_username))}
								>
									{chat.other_person_username}
								</div>
							)}
						</div>
					</div>
				</>
			}
		</div>
	);
};

export default Sidebar;
