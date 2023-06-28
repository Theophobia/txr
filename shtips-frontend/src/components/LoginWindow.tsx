import React, { useState } from 'react';
import Modal from 'react-modal';
import axios from "axios/index";
import useAuth from "../useAuth";

const LoginWindow = ({ isOpen, onClose}) => {
	const [username, setUsername] = useState('');
	const [password, setPassword] = useState('');
	const { loggedIn, userInfo, login, logout } = useAuth();

	const handleLogin = async () => {
		await login(username, password);
	};

	return (
		<Modal isOpen={isOpen} onRequestClose={onClose}>
			<h2>Login</h2>
			<form>
				<input
					type="text"
					placeholder="Username"
					value={username}
					onChange={e => setUsername(e.target.value)}
				/>
				<input
					type="password"
					placeholder="Password"
					value={password}
					onChange={e => setPassword(e.target.value)}
				/>
				<button type="button" onClick={handleLogin}>Login</button>
			</form>
		</Modal>
	);
};

export default LoginWindow;
