import { combineReducers } from "redux";
import authReducer from "./authReducer";
import visibilityReducer from "./visibilityReducer";

const rootReducer = combineReducers({
	auth: authReducer,
	visibility: visibilityReducer,
	// Other reducers
});

export default rootReducer;
