export const loginSuccess = (userData, token) => ({
	type: "LOGIN_SUCCESS",
	payload: { userData, token },
});

export const logout = () => ({
	type: "LOGOUT",
});
