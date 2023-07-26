import React from "react";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import "./App.css";
import LoginPage from "./components/LoginPage";
import MainPage from "./components/MainPage";
import ChatPage from "./components/ChatPage";
import Sidebar from "./components/Sidebar";
import RegisterPage from "./components/RegisterPage";
import SearchPage from "./components/SearchPage";
import UserProfilePage from "./components/UserProfilePage";

function App() {

	return (
		<div className={"app"}>
			<Router>
				<Sidebar/>
				<Routes>
					<Route path="/" element={<MainPage/>}/>
					<Route path="/login" element={<LoginPage/>} />
					<Route path="/register" element={<RegisterPage/>} />
					<Route path="/chat/:username" element={<ChatPage/>}/>
					<Route path="/profile" element={<UserProfilePage/>}/>
					<Route path="/search" element={<SearchPage/>}/>
				</Routes>
			</Router>
		</div>
	);
}

export default App;
