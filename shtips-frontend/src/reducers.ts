import { combineReducers } from "redux";
import authReducer from "./state/authReducer";
import visibilityReducer from "./state/visibilityReducer";

const rootReducer = combineReducers({
	auth: authReducer,
	visibility: visibilityReducer,
	// Other reducers
});

export default rootReducer;
