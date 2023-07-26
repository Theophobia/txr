import {ActivityStatusEnum} from "../api/activityStatus";

export const local = (status: ActivityStatusEnum) => ({
	type: "LOCAL",
	payload: {status}
});

export const remote = (username: string, status: ActivityStatusEnum) => ({
	type: "REMOTE",
	payload: {username, status},
});
