export const loginSuccess = (userData, token) => ({
	type: "LOGIN_SUCCESS",
	payload: {userData, token},
});

export const logout = () => ({
	type: "LOGOUT",
});

export const registerSuccess = (userData, token) => ({
	type: "REGISTER_SUCCESS",
	payload: {userData, token},
});
