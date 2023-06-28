import LoginButton from "./LoginButton";
import RegisterButton from "./RegisterButton";
import React from "react";
import useAuth from "../useAuth";

const MainPage = () => {

	const {isLoggedIn, isLoading, userInfo, logout} = useAuth();

	const handleLogout = () => {
		logout().then(() => {});
	};

	return (<div className={"main"}>
		<h1>My App</h1>
		<div>
			{(isLoading
					? <div></div>
					: (isLoggedIn
							? (
								<div>
									<p>Welcome, {userInfo?.username}!</p>
									<button onClick={handleLogout}>Logout</button>
								</div>
							) : (
								<div>
									<LoginButton/>
									<RegisterButton/>
								</div>
							)
					)
			)}
		</div>
	</div>);
}

export default MainPage;
