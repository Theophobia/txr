import {useDispatch, useSelector} from "react-redux";
import { useNavigate } from "react-router-dom";
import UserData from "../api/userData";
import {AuthState} from "../state/authState";
import {loginSuccess, logout} from "../state/authActions";
import React from "react";
import Sidebar from "./Sidebar";
import {sidebarHidden} from "../state/visibilityActions";

const MainPage = () => {
	// const userData: UserData | null = useSelector((state) => state.authentication.userData);
	const auth: AuthState = useSelector((state) => state.auth);

	const dispatch = useDispatch();
	const navigate = useNavigate();

	const dispatchLogout = async () => {
		dispatch(logout());
		dispatch(sidebarHidden());
	};

	return (
		<div>
			{auth.isLoggedIn ?
				<div>
					<button onClick={() => dispatchLogout()}>Log out</button>
				</div>
				:
				<div>Not logged in!</div>}
		</div>
	);
};

export default MainPage;
