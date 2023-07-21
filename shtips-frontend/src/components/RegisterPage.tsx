import {useState} from "react";
import { useNavigate } from "react-router-dom";
import UserData from "../api/userData";
import {loginSuccess, registerSuccess} from "../state/authActions";
import {useDispatch, useSelector} from "react-redux";
import {sidebarShown} from "../state/visibilityActions";
import {AuthState} from "../api/authState";
import {apiUserInfo, apiUserLogin, apiUserRegister} from "../util/query";
import "./RegisterPage.css";

function RegisterPage() {
	const [email, setEmail] = useState<string | null>(null);
	const [username, setUsername] = useState<string | null>(null);
	const [password, setPassword] = useState<string | null>(null)

	const dispatch = useDispatch();
	const navigate = useNavigate();

	const auth: AuthState = useSelector((state) => state.auth);

	const registerUser = async (email, username, password, dispatch) => {
		if (auth.isLoggedIn) {
			console.log("Already logged in! Not sending login request. Should show error");
			return;
		}

		try {
			// Send authentication request
			const response = await apiUserRegister(email, username, password);

			if (response === null) {
				console.error("Register response is null!");
				throw new Error();
			}
			if (!response.ok) {
				console.error("Error registering!");
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
				console.error("Error getting user data while registering!");
				throw new Error();
			}

			// Extract the user data from the response
			const user: UserData = await userDataResponse.json();
			console.log(user);

			// Dispatch actions to update the store with authentication details and user data
			dispatch(registerSuccess(user, token));
			dispatch(sidebarShown());

			// Redirect to main page
			navigate("/");
		}
		catch (error) {
			// Handle authentication error
			// dispatch(registerFailure(error.message));
		}
	};

	return (
		<div className={"register_root"}>
			<div className={"register_container"}>
				<div className={"flex_centered"}>
					<input placeholder={"Email"}
						   onChange={(event) => setEmail(event.target.value)}
						   className={"input"}
					/>
				</div>

				<div className={"flex_centered"}>
					<input placeholder={"Username"}
						   onChange={(event) => setUsername(event.target.value)}
						   className={"input"}
					/>
				</div>

				<div className={"flex_centered"}>
					<input placeholder={"Password"}
						   onChange={(event) => setPassword(event.target.value)}
						   onKeyDownCapture={(event) => {
							   if (event.key === "Enter") {
								   registerUser(email, username, password, dispatch);
							   }
						   }}
						   className={"input"}
					/>
				</div>

				<div className={"flex_centered"}>
					<button onClick={() => {
						registerUser(email, username, password, dispatch);
					}}>
						Submit
					</button>
				</div>
			</div>
		</div>
	);
}

export default RegisterPage;
