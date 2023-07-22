import {useDispatch, useSelector} from "react-redux";
import {local} from "../state/activityActions";
import {AuthState} from "./authState";

export enum ActivityStatusEnum {
	ONLINE, DO_NOT_DISTURB, AWAY, OFFLINE
}

export interface ActivityStatusState {
	local: ActivityStatusEnum,
	remote: Map<string, ActivityStatusEnum>,
}

export const activityToColor = (activity: ActivityStatusEnum) => {
	switch (activity) {
		case ActivityStatusEnum.ONLINE: {
			return "#00FF00";
		}
		case ActivityStatusEnum.DO_NOT_DISTURB: {
			return "#FF0000";
		}
		case ActivityStatusEnum.AWAY: {
			return "#FF9900"
		}
		case ActivityStatusEnum.OFFLINE: {
			return "#969696"
		}
		default: {
			console.error("Switch in activityToColor is incomplete");
			return "";
		}
	}
};
