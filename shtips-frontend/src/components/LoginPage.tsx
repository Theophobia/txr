import {useState} from "react";
import { useNavigate } from "react-router-dom";
import UserData from "../api/userData";
import {loginSuccess} from "../state/authActions";
import {useDispatch, useSelector} from "react-redux";
import {sidebarShown} from "../state/visibilityActions";
import {AuthState} from "../state/authState";

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
			const authResponse = await fetch(`http://localhost:8080/api/user/login
			?usernameOrEmail=${encodeURIComponent(usernameOrEmail)}
			&password=${encodeURIComponent(password)}`, {
				method: "POST"
			});

			if (!authResponse.ok) {
				return;
			}

			// Extract the token from the response
			const token: string = await authResponse.text();

			// Send request to fetch user data
			const userDataResponse = await fetch(`http://localhost:8080/api/user/info
			?token=${encodeURIComponent(token)}`, {
				method: "GET"
			});

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
