import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

const LoginButton = () => {
	const navigate = useNavigate();
	const [redirect, setRedirect] = useState(false);

	const handleRedirect = () => {
		setRedirect(true);
	}

	useEffect(() => {
		if (redirect) {
			navigate("/login");
		}
	}, [redirect]);

	return (
		<button onClick={handleRedirect}>Login</button>
	);
};

export default LoginButton;
