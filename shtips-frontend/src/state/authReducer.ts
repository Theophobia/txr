import {AuthState} from "./authState";

const initialState: AuthState = {
	isLoggedIn: false,
	userData: null,
	token: null,
};

let authReducer: (state: AuthState, action) => (AuthState) = (state= initialState, action) => {
	switch (action.type) {
		case "LOGIN_SUCCESS":
			return {
				...state,
				isLoggedIn: true,
				userData: action.payload.userData,
				token: action.payload.token,
			};
		case "LOGOUT":
			return {
				...state,
				isLoggedIn: false,
				userData: null,
				token: null,
			};
		// Additional cases for handling other actions
		default:
			return state;
	}
};

export default authReducer;
