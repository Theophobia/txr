import {VisibilityState} from "./visibilityState";

const initialState: VisibilityState = {
	isSidebarVisible: false
};

let visibilityReducer: (state: VisibilityState, action) => (VisibilityState) = (state= initialState, action) => {
	switch (action.type) {
		case "SIDEBAR_SHOWN":
			return {
				...state,
				isSidebarVisible: true,
			};
		case "SIDEBAR_HIDDEN":
			return {
				...state,
				isSidebarVisible: false,
			}
		// Additional cases for handling other actions
		default:
			return state;
	}
};

export default visibilityReducer;
