import {useState} from "react";
import { useNavigate } from "react-router-dom";
import UserData from "../api/userData";
import {loginSuccess} from "../state/authActions";
import {useDispatch, useSelector} from "react-redux";
import {sidebarShown} from "../state/visibilityActions";
import {AuthState} from "../state/authState";
import apiUserLogin = Query.apiUserLogin;
import apiUserInfo = Query.apiUserInfo;

function LoginPage() {
	const [usernameOrEmail, setUsernameOrEmail] = useState<string | null>(null);
	const [password, setPassword] = useState<string | null>(null)

	const dispatch = useDispatch();
	const navigate = useNavigate();

	const auth: AuthState = useSelector((state) => state.auth);

	const authenticateUser = async (usernameOrEmail, password, dispatch) => {
		if (auth.isLoggedIn) {
			console.log("Already logged in! Not sending login request. Should show error");
			return;
		}

		try {
			// Send authentication request
			const response = await apiUserLogin(usernameOrEmail, password);

			if (response === null) {
				throw new Error();
			}

			if (!response.ok) {
				return;
			}

			// Extract the token from the response
			const token: string = await response.text();

			// Send request to fetch user data
			const userDataResponse = await apiUserInfo(token);

			if (userDataResponse === null) {
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
		<div>
			{}
			<input placeholder={"Username/Email"}
				   onChange={(event) => setUsernameOrEmail(event.target.value)}
			/>

			<input placeholder={"Password"}
				   onChange={(event) => setPassword(event.target.value)}
			/>

			<button onClick={() => {
				authenticateUser(usernameOrEmail, password, dispatch);
			}}>
				Submit
			</button>
		</div>
	);
}

export default LoginPage;
