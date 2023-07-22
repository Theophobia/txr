import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {AuthState} from "../api/authState";
import {logout} from "../state/authActions";
import React from "react";
import {sidebarHidden} from "../state/visibilityActions";
import useActivity from "./useActivity";
import {ActivityStatusEnum} from "../api/activityStatus";

const MainPage = () => {
	// const userData: UserData | null = useSelector((state) => state.authentication.userData);
	const auth: AuthState = useSelector((state) => state.auth);

	const dispatch = useDispatch();
	const navigate = useNavigate();
	const activityHook = useActivity();

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
					<button onClick={() => activityHook.setActivity("aelar", ActivityStatusEnum.ONLINE)}>Online</button>
					<button onClick={() => activityHook.setActivity("aelar", ActivityStatusEnum.AWAY)}>Away</button>
					<button onClick={() => activityHook.setActivity("aelar", ActivityStatusEnum.DO_NOT_DISTURB)}>DND</button>
					<button onClick={() => activityHook.setActivity("aelar", ActivityStatusEnum.OFFLINE)}>Offline</button>
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
