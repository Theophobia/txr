import React from "react";
import {BrowserRouter as Router, Switch, Route} from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Chat from "./components/Chat";
import "./App.css";

function App() {
	return (
		<Router>
			<div className="app">
				<Sidebar/>
				<Switch>
					<Route path="/chat/:userId">
						<Chat/>
					</Route>
				</Switch>
			</div>
		</Router>
	);
}

export default App;
