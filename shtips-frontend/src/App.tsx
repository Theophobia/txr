import React, {useEffect, useState} from "react";
import {BrowserRouter as Router, Routes, Route, useNavigate} from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Chat from "./components/Chat";
import LoginWindow from "./components/LoginWindow";
import RegisterWindow from "./components/RegisterWindow";
import MainPage from "./components/MainPage";
import "./App.css";

function App() {

	return (
		<div className={"app"}>
			<Router>
				<Sidebar/>
				<Routes>
					<Route path="/" element={<MainPage/>}/>
					<Route path="/login" element={<LoginWindow/>} />
					<Route path="/register" element={<RegisterWindow/>} />
					<Route path="/chat/:username" element={<Chat/>}/>
				</Routes>
			</Router>
		</div>
	);
}

export default App;
