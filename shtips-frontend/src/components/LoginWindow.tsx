import React, { useState } from 'react';
import Modal from 'react-modal';
import { useNavigate } from 'react-router-dom';
import useAuth from "../useAuth";

const LoginWindow = () => {
	const [username, setUsername] = useState('');
	const [password, setPassword] = useState('');

	const {login} = useAuth();
	const navigate = useNavigate();

	const handleLogin = async () => {
		await login(username, password, () => navigate("/", {token: "asd"}));
	};

	const handleEnter = async (event) => {
		if (event.key === "Enter") {
			await handleLogin();
		}
	};

	return (
		<div>
			<h2>Login</h2>
			<form>
				<input
					type="text"
					placeholder="Username"
					value={username}
					onChange={e => setUsername(e.target.value)}
					onKeyDown={handleEnter}
				/>
				<input
					type="password"
					placeholder="Password"
					value={password}
					onChange={e => setPassword(e.target.value)}
					onKeyDown={handleEnter}
				/>
				<button type="button" onClick={handleLogin}>Login</button>
			</form>
		</div>
	);
};

export default LoginWindow;
