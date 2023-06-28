import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

const RegisterButton = () => {
	const navigate = useNavigate();
	const [redirect, setRedirect] = useState(false);

	const handleRedirect = () => {
		setRedirect(true);
	}

	useEffect(() => {
		if (redirect) {
			navigate("/register")
		}
	}, [redirect]);

	return (
		<button onClick={handleRedirect}>Register</button>
	);
};

export default RegisterButton;
