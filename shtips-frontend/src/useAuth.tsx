import {useEffect, useState} from "react";
import axios from "axios";

class PasswordlessUser {
	userId: number
	email: string
	username: string
}

const useAuth = () => {
	const [token, setToken] = useState<null | string>(null);
	const [userInfo, setUserInfo] = useState<null | PasswordlessUser>(null);

	const [loggedIn, setLoggedIn] = useState(false);

	const login = async (usernameOrEmail: string, password: string) => {
		try {
			const data = {usernameOrEmail, password};

			const response = await axios.post('http://localhost:8080/api/user/login', {}, {params: data});
			const token: string = response.data;

			setLoggedIn(true);
			setToken(token);

			localStorage.setItem('authToken', token);
		} catch (error) {
			// Handle login error
			console.error('Login failed', error);
		}
	};

	const logout = () => {
		setLoggedIn(false);
		setUserInfo(null);
		localStorage.removeItem('authToken');
	};

	const fetchUserInfo = async () => {
		try {
			const data = {token: token};
			const response = await axios.get('http://localhost:8080/api/user/info', {params: data});
			const userInfo = response.data;
			setUserInfo(userInfo);
		} catch (error) {
			// Handle fetch user info error
			console.error('Failed to fetch user info', error);
		}
	}

	const register = async (email: string, username: string, password: string) => {
		try {
			const data = {
				email,
				username,
				password
			};
			const response = await axios.post('http://localhost:8080/api/user/register', {}, {params: data});
			const token: string = response.data;

			setLoggedIn(true);
			setToken(token);

			localStorage.setItem('authToken', token);
		} catch (error) {
			// Handle registration error
			console.error('Registration failed', error);
		}
	};

	useEffect(() => {
		if (token !== null) {
			fetchUserInfo().then(r => {});
		}
	}, [token]);

	return { loggedIn, userInfo, login, logout, register};
};

export default useAuth;
