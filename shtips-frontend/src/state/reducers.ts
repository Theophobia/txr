import { combineReducers } from "redux";
import authReducer from "./authReducer";
import visibilityReducer from "./visibilityReducer";
import activityReducer from "./activityReducer";

const rootReducer = combineReducers({
	auth: authReducer,
	visibility: visibilityReducer,
	activity: activityReducer,
	// Other reducers
});

export default rootReducer;
