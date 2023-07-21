import React, {useEffect, useState} from "react";
import {AuthState} from "../state/authState";
import {apiUserSearch} from "../util/query";
import {useNavigate} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import "./SearchPage.css";

const SearchPage = () => {

	const [searchTerm, setSearchTerm] = useState<string>("");
	const [searchResults, setSearchResults] = useState<string[]>([]);

	const dispatch = useDispatch();
	const navigate = useNavigate();
	const auth: AuthState = useSelector((state) => state.auth);

	const searchUser = async (event, userId: number, token: string, searchTerm: string) => {
		const response = await apiUserSearch(userId, token, searchTerm);

		if (response === null) {
			return;
		}

		const data: string[] = await response.json();
		console.info("searchUser:", data);
		setSearchResults(data);

		event.target.value = "";
	}

	// Redirect useEffect
	useEffect(() => {
		if (!auth.isLoggedIn) {
			navigate("/");
			return;
		}
	});

	return (
		<>{!auth.isLoggedIn
			?
			<>
				<div>You are not logged in! You should return to the login page.</div>
			</>
			:
			<>
				<div>
					<div>
						<input placeholder={"Password"}
							   onChange={(event) => setSearchTerm(event.target.value)}
							   onKeyDownCapture={(event) => {
								   if (event.key === "Enter") {
									   if (auth.userData && auth.token) {
										   searchUser(event, auth.userData.userId, auth.token, searchTerm);
									   }
								   }
							   }}
						>

						</input>
					</div>
					<div>
						{searchResults && searchResults.length !== 0 && searchResults.map(value =>
							<div key={value}
								 className={"search_result"}
								 onClick={() => navigate("/chat/".concat(value))}>
								{value}
							</div>)}
					</div>
				</div>
			</>
		}</>
	);
};

export default SearchPage;
