import React from "react";
import {BrowserRouter as Router, Routes, Route, useNavigate} from "react-router-dom";
import "./App.css";
import LoginPage from "./components/LoginPage";
import MainPage from "./components/MainPage";
import Chat from "./components/Chat";
import Sidebar from "./components/Sidebar";
import RegisterPage from "./components/RegisterPage";

function App() {

	return (
		<div className={"app"}>
			<Router>
				<Sidebar/>
				<Routes>
					<Route path="/" element={<MainPage/>}/>
					<Route path="/login" element={<LoginPage/>} />
					<Route path="/register" element={<RegisterPage/>} />
					<Route path="/chat/:username" element={<Chat/>}/>
				</Routes>
			</Router>
		</div>
	);
}

export default App;
