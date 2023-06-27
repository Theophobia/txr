import React from "react";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Chat from "./components/Chat";
import "./App.css";

function App() {
	return (
		<>
			<Sidebar/>
			<Router>
				<Routes>
					<Route path="/" element={<div/>}/>
					<Route path="/chat/:username" element={<Chat/>}/>
				</Routes>
			</Router>
		</>
	);
}

export default App;
