import React, { useState } from 'react';
import Modal from 'react-modal';
import axios from "axios/index";
import useAuth from "../useAuth";

const LoginWindow = ({ isOpen, onClose}) => {
	const [username, setUsername] = useState('');
	const [email, setEmail] = useState('');
	const [password, setPassword] = useState('');
	const { register } = useAuth();

	const handleRegister = async () => {
		await register(email, username, password);
	};

	return (
		<Modal isOpen={isOpen} onRequestClose={onClose}>
			<h2>Register</h2>
			<form>
				<input
					type="text"
					placeholder="Email"
					value={email}
					onChange={e => setEmail(e.target.value)}
				/>
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
				<button type="button" onClick={handleRegister}>Register</button>
			</form>
		</Modal>
	);
};

export default LoginWindow;
