import {useState} from "react";
import { useNavigate } from "react-router-dom";
import UserData from "../api/userData";
import {loginSuccess} from "../state/authActions";
import {useDispatch, useSelector} from "react-redux";
import {sidebarShown} from "../state/visibilityActions";
import {AuthState} from "../api/authState";
import {apiUserInfo, apiUserLogin} from "../util/query";
import "./LoginPage.css";

function LoginPage() {
	const [usernameOrEmail, setUsernameOrEmail] = useState<string | null>(null);
	const [password, setPassword] = useState<string | null>(null)

	const dispatch = useDispatch();
	const navigate = useNavigate();

	const auth: AuthState = useSelector((state) => state.auth);

	const loginUser = async (usernameOrEmail, password, dispatch) => {
		if (auth.isLoggedIn) {
			console.log("Already logged in! Not sending login request. Should show error");
			return;
		}

		try {
			// Send authentication request
			const response = await apiUserLogin(usernameOrEmail, password);

			if (response === null) {
				console.error("Login response is null!");
				throw new Error();
			}

			if (!response.ok) {
				console.error("Error logging in!");
				throw new Error();
			}

			// Extract the token from the response
			const token: string = await response.text();

			// Send request to fetch user data
			const userDataResponse = await apiUserInfo(token);

			if (userDataResponse === null) {
				throw new Error();
			}
			if (!userDataResponse.ok) {
				console.error("Error getting user data while logging in!");
				throw new Error();
			}

			// Extract the user data from the response
			const user: UserData = await userDataResponse.json();
			console.log(user);

			// Dispatch actions to update the store with authentication details and user data
			dispatch(loginSuccess(user, token));
			dispatch(sidebarShown());

			// Redirect to main page
			navigate("/");
		}
		catch (error) {
			// Handle authentication error
			// dispatch(loginFailure(error.message));
		}
	};

	return (
		<div className={"login_root"}>
			<div className={"login_container"}>
				<div className={"flex_centered"}>
					<input placeholder={"Username/Email"}
						   onChange={(event) => setUsernameOrEmail(event.target.value)}
						   className={"input"}
					/>
				</div>

				<div className={"flex_centered"}>
					<input placeholder={"Password"}
						   onChange={(event) => setPassword(event.target.value)}
						   onKeyDownCapture={(event) => {
							   if (event.key === "Enter") {
								   loginUser(usernameOrEmail, password, dispatch);
							   }
						   }}
						   className={"input"}
					/>
				</div>

				<div className={"flex_centered"}>
					<button onClick={() => loginUser(usernameOrEmail, password, dispatch)}>
						Submit
					</button>
				</div>
			</div>
		</div>
	);
}

export default LoginPage;
