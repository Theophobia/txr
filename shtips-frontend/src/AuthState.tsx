import {useState} from "react";

interface AuthResponse {
	token: string;
}

const AuthState = () => {
	const [authToken, setAuthToken] = useState<string | null>(null);

	const login = async (usernameOrEmail: string, password: string, callback?: () => void) => {
		try {
			const response = await fetch(`http://localhost:8080/api/user/login?usernameOrEmail=${encodeURIComponent(usernameOrEmail)}&password=${encodeURIComponent(password)}`, {
				method: "POST"
			});
			if (response.ok) {
				const data: AuthResponse = {token: await response.text()};
				const token = data.token;

				console.log(`Token received from login: ${token}`);
				setAuthToken(token);
				localStorage.setItem('authToken', token);

				if (callback) {
					callback();
				}
				
				return AuthState;
			}
		}
		catch (error) {
			console.error('Error logging in:', error);
		}
	};

	return this;
}
