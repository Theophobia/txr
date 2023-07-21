import {useNavigate, useParams} from "react-router-dom";
import {AuthState} from "../state/authState";
import {useDispatch, useSelector} from "react-redux";
import React, {useEffect, useState} from "react";
import useWebSocket from "./UseWebSocket";
import Message, {MessageStatus} from "../api/message";
import {Event12, Event14} from "../api/event";

const UserProfilePage = () => {

	const [showEmail, setShowEmail] = useState(false);
	const [formattedEmail, setFormattedEmail] = useState("");

	const auth: AuthState = useSelector((state) => state.auth);
	const dispatch = useDispatch();
	const navigate = useNavigate();

	const {send} = useWebSocket({
		onNewMessage: (msg: Message) => {},
		onMessageConfirm: (event12: Event12) => {},
		onMessageFetched: (event14: Event14) => {}
	});

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
			<div>
				<img className={"msg_img_other"} src={`http://localhost:8080/api/user/avatar?username=${auth.userData.username}`}></img>
				<div>
					Username: {auth.userData.username}
				</div>
				<div>
					Email: {formattedEmail}
				</div>
				<button onClick={() => setShowEmail(!showEmail)}>Reveal</button>
			</div>
	}</>;
};

export default UserProfilePage;
