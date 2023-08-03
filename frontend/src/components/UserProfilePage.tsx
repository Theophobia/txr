import {useNavigate, useParams} from "react-router-dom";
import {AuthState} from "../api/authState";
import {useDispatch, useSelector} from "react-redux";
import React, {useEffect, useState} from "react";
import useWebSocket from "./UseWebSocket";
import Message, {MessageStatus} from "../api/message";
import {Event12, Event14} from "../api/event";
import AvatarComponent from "./AvatarComponent";
import "./UserProfilePage.scss";

const UserProfilePage = () => {

	const [showEmail, setShowEmail] = useState(false);
	const [formattedEmail, setFormattedEmail] = useState("");

	const auth: AuthState = useSelector((state) => state.auth);
	const dispatch = useDispatch();
	const navigate = useNavigate();

	const formatEmail = (email: string) => {
		const parts = email.split('@');
		parts[0] = "********";
		return parts[0] + '@' + parts[1];
	}

	useEffect(() => {
		if (auth.userData === null) {
			return;
		}

		setFormattedEmail(showEmail ? auth.userData.email : formatEmail(auth.userData.email));
	}, [showEmail]);

	return <>{
		!auth.isLoggedIn || auth.userData === null || auth.token === null ?
			<>

			</>
		:
			<div className={"profile_container_1"}>
				<div className={"profile_container_2"}>
					<AvatarComponent username={auth.userData.username} hasActivity={false} imgRadius={64}/>

					<div style={{paddingBottom: "10px"}}/>

					<div className={"profile_container_3"}>
						<div className={"profile_column_1"}>
							<div>Username:</div>
							<div>Email:</div>
						</div>
						<div className={"profile_column_spacer"}/>
						<div className={"profile_column_2"}>
							<div>{auth.userData.username}</div>
							<div>{formattedEmail}</div>
						</div>
					</div>

					<button onClick={() => setShowEmail(!showEmail)}>Reveal</button>
				</div>
			</div>
	}</>;
};

export default UserProfilePage;
