import React, { useState } from 'react';
import Modal from 'react-modal';
import { useNavigate } from 'react-router-dom';
import useAuth from "../useAuth";

const LoginWindow = () => {
	const [username, setUsername] = useState('');
	const [email, setEmail] = useState('');
	const [password, setPassword] = useState('');

	const { register } = useAuth();
	const navigate = useNavigate();

	const handleRegister = async () => {
		await register(email, username, password, () => navigate("/"));
	};

	const handleEnter = async (event) => {
		if (event.key === "Enter") {
			await handleRegister();
		}
	};

	return (
		<div>
			<h2>Register</h2>
			<form>
				<input
					type="text"
					placeholder="Email"
					value={email}
					onChange={e => setEmail(e.target.value)}
					onKeyDown={handleEnter}
				/>
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
				<button type="button" onClick={handleRegister}>Register</button>
			</form>
		</div>
	);
};

export default LoginWindow;
