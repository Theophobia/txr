import React, {useEffect, useState} from "react";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Chat from "./components/Chat";
import "./App.css";
import useAuth from "./useAuth";
import LoginWindow from "./components/LoginWindow";
import RegisterWindow from "./components/RegisterWindow";

function App() {

	const {loggedIn, userInfo, login, logout} = useAuth();
	const [showLoginWindow, setShowLoginWindow] = useState(false);
	const [showRegisterWindow, setShowRegisterWindow] = useState(false);

	const handleLogout = () => {
		logout();
	};

	const handleLoginClick = () => {
		setShowLoginWindow(true);
	};

	const handleLoginWindowClose = () => {
		setShowLoginWindow(false);
	};

	const handleRegisterClick = () => {
		setShowRegisterWindow(true);
	};

	const handleRegisterWindowClose = () => {
		setShowRegisterWindow(false);
	};

	return (
		<div>
			<header>
				<h1>My App</h1>
				{loggedIn
					? (
						<div>
							<p>Welcome, {userInfo?.username}!</p>
							<button onClick={handleLogout}>Logout</button>
						</div>
					) : (
						<div>
							<button onClick={handleLoginClick}>Login</button>
							<button onClick={handleRegisterClick}>Register</button>
						</div>
					)
				}
			</header>
			{loggedIn ?
				<div>
					<Sidebar/>
					<Router>
						<Routes>
							<Route path="/" element={<div></div>}/>
							<Route path="/chat/:username" element={<Chat/>}/>
						</Routes>
					</Router>
				</div>
				:
				<div>
					<LoginWindow
						isOpen={showLoginWindow}
						onClose={handleLoginWindowClose}
					/>
					<RegisterWindow
						isOpen={showRegisterWindow}
						onClose={handleRegisterWindowClose}
					/>
				</div>
			}
		</div>
	);
}

export default App;
