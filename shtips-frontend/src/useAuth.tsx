import { useEffect, useState } from 'react';

interface UserInfo {
	userId: number;
	email: string;
	username: string;
}

interface AuthResponse {
	token: string;
}

export interface AuthHook {
	isLoading: boolean;
	userInfo: UserInfo | null;
	logout: (callback?: () => void) => Promise<void>;
	authToken: string | null;
	isLoggedIn: boolean;
	login: (usernameOrEmail: string, password: string, callback?: () => void) => Promise<void>;
	register: (username: string, email: string, password: string, callback?: () => void) => Promise<void>
}

const useAuth = () => {
	const [authToken, setAuthToken] = useState<string | null>(null);
	const [userInfo, setUserInfo] = useState<UserInfo | null>(null);
	const [isLoggedIn, setIsLoggedIn] = useState(false);
	const [isLoading, setIsLoading] = useState(true);

	useEffect(() => {
		// Check if auth token exists in local storage on initial load
		const token = localStorage.getItem('authToken');
		if (token) {
			setAuthToken(token);
			checkTokenValidity(token).then(() => {});
		}
		else {
			setIsLoading(false);
		}
	}, []);

	const checkTokenValidity = async (token: string) => {
		try {
			setIsLoading(true);
			const response = await fetch(`http://localhost:8080/api/user/valid?token=${encodeURIComponent(token)}`);
			if (response.status === 200) {
				setIsLoggedIn(true);
				await getUserInfo(token);
			}
			else {
				setIsLoggedIn(false);
				setAuthToken(null);
				localStorage.removeItem('authToken');
			}
			setIsLoading(false);
		}
		catch (error) {
			console.error('Error checking token validity:', error);
			setIsLoading(false);
		}
	};

	const login = async (usernameOrEmail: string, password: string, callback?: () => void) => {
		try {
			setIsLoading(true);
			const response = await fetch(`http://localhost:8080/api/user/login?usernameOrEmail=${encodeURIComponent(usernameOrEmail)}&password=${encodeURIComponent(password)}`, {
				method: "POST"
			});
			if (response.ok) {
				const data: AuthResponse = {token: await response.text()};
				const token = data.token;

				console.log(`Token received from login: ${token}`);
				setAuthToken(token);
				localStorage.setItem('authToken', token);
				setIsLoggedIn(true);
				setIsLoading(false);
				await getUserInfo(token);

				if (callback) {
					callback();
				}
			}
			else {
				setIsLoggedIn(false);
			}
		}
		catch (error) {
			console.error('Error logging in:', error);
		}
	};

	const register = async (username: string, email: string, password: string, callback?: () => void) => {
		try {
			setIsLoading(true);
			const response = await fetch(`http://localhost:8080/api/user/register?username=${encodeURIComponent(username)}&email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`, {
				method: "POST"
			});
			if (response.ok) {
				const data: AuthResponse = {token: await response.text()};
				const token = data.token;

				console.log(`Token received from register: ${token}`);
				setAuthToken(token);
				localStorage.setItem('authToken', token);
				setIsLoggedIn(true);
				await getUserInfo(token);

				if (callback) {
					callback();
				}
			}
			else {
				setIsLoggedIn(false);
			}
		} catch (error) {
			console.error('Error registering:', error);
		}
	};


	const logout = async (callback?: () => void) => {
		try {
			if (authToken === null) {
				return;
			}
			const response = await fetch(`http://localhost:8080/api/user/logout?token=${encodeURIComponent(authToken)}`, {
				method: "POST"
			});
			if (response.ok) {
				setAuthToken(null);
				setUserInfo(null);
				setIsLoggedIn(false);
				localStorage.removeItem('authToken');

				if (callback) {
					callback();
				}
			}
			else {
				console.error('Error logging out:', response.statusText);
			}
		}
		catch (error) {
			console.error('Error logging out:', error);
		}
	};

	const getUserInfo = async (token: string) => {
		try {
			setIsLoading(true);
			const response = await fetch(`http://localhost:8080/api/user/info?token=${encodeURIComponent(token)}`);
			if (response.ok) {
				const data: UserInfo = await response.json();
				setUserInfo(data);
				setIsLoading(false);
			}
			else {
				console.error('Error fetching user info:', response.statusText);
			}
		}
		catch (error) {
			console.error('Error fetching user info:', error);
		}
	};

	const res: AuthHook = {
		authToken,
		userInfo,
		isLoggedIn,
		isLoading,
		login,
		register,
		logout
	};

	return res;
};

export default useAuth;
