import LoginButton from "./LoginButton";
import RegisterButton from "./RegisterButton";
import React, {useContext, useEffect} from "react";
import useAuth from "../useAuth";
import {AuthContext} from "../AuthContext";

const MainPage = () => {

	// const {isLoggedIn, isLoading, userInfo, logout} = useAuth();
	const auth = useContext(AuthContext);

	const handleLogout = () => {
		auth?.logout().then(() => {});
		// logout().then(() => {});
	};

	useEffect(() => {}, [auth?.isLoading]);

	return (
		<div className={"main"}>
		<h1>My App</h1>
			{console.log("Entering main page")}
			{console.log(`auth = ${auth}`)}
			{console.log(`auth?.isLoggedIn = ${auth?.isLoggedIn}`)}
			{console.log(`auth?.isLoading = ${auth?.isLoading}`)}
			{console.log(`auth?.authToken = ${auth?.authToken}`)}
		<div>
			{auth === null ? <div></div> : auth.isLoggedIn ? (
				<div>
					<p>Welcome, {auth.userInfo?.username}!</p>
					<button onClick={handleLogout}>Logout</button>
				</div>
			): (
				<div>
					<LoginButton/>
					<RegisterButton/>
				</div>
			)}
		</div>
	</div>);
}

export default MainPage;
