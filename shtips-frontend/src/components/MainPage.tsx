import {useDispatch, useSelector} from "react-redux";
import { useNavigate } from "react-router-dom";
import UserData from "../api/userData";
import {AuthState} from "../api/authState";
import {loginSuccess, logout} from "../state/authActions";
import React from "react";
import Sidebar from "./Sidebar";
import {sidebarHidden} from "../state/visibilityActions";
import AvatarComponent from "./AvatarComponent";

const MainPage = () => {
	// const userData: UserData | null = useSelector((state) => state.authentication.userData);
	const auth: AuthState = useSelector((state) => state.auth);

	const dispatch = useDispatch();
	const navigate = useNavigate();

	const dispatchLogout = async () => {
		dispatch(logout());
		dispatch(sidebarHidden());
	};

	const redirectToLoginPage = () => {
		navigate("/login");
	};

	const redirectToRegisterPage = () => {
		navigate("/register");
	};

	return (
		<div>
			{auth.isLoggedIn
				?
				<div>
					<button onClick={() => dispatchLogout()}>Log out</button>
				</div>
				:
				<>
					<div>
						Not logged in!
					</div>
					<button onClick={() => redirectToLoginPage()}>
						Login
					</button>
					<button onClick={() => redirectToRegisterPage()}>
						Register
					</button>
				</>
			}
		</div>
	);
};

export default MainPage;
