import {useEffect, useState} from "react";

const useAuth = () => {
	const [token, setToken] = useState("");

	useEffect(() => {
		setToken("DSPGlZ5jKpg2KX8WjfChR6v3SszU6oIqBPsidS5Ki70=");
	}, []);

	return token;
};

export default useAuth;
