import {ActivityStatusEnum, ActivityStatusState} from "../api/activityStatus";

const initialState: ActivityStatusState = {
	local: ActivityStatusEnum.OFFLINE,
	remote: new Map<string, ActivityStatusEnum>(),
};

let activityReducer: (state: ActivityStatusState, action) => (ActivityStatusState) = (state= initialState, action) => {
	switch (action.type) {
		case "LOCAL":
			return {
				...state,
				local: action.payload.status
			};

		case "REMOTE":
			state.remote[action.payload.username] = action.payload.status;
			return state;

		// Additional cases for handling other actions
		default:
			return state;
	}
};

export default activityReducer;
