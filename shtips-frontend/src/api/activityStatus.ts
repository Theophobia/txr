export enum ActivityStatusEnum {
	ONLINE = "ONLINE",
	DO_NOT_DISTURB = "DO_NOT_DISTURB",
	AWAY = "AWAY",
	OFFLINE = "OFFLINE"
}

export interface ActivityStatusState {
	local: ActivityStatusEnum,
	remote: Map<string, ActivityStatusEnum>,
}

export const activityToColor = (activity: ActivityStatusEnum) => {
	switch (activity) {
		case ActivityStatusEnum.ONLINE:
			return "#00FF00";
		case ActivityStatusEnum.DO_NOT_DISTURB:
			return "#FF0000";
		case ActivityStatusEnum.AWAY:
			return "#FF9900";
		case ActivityStatusEnum.OFFLINE:
			return "#969696";
		default: {
			console.error("Switch in activityToColor is incomplete", activity);
			return "#000000";
		}
	}
};

export const activityToString = (activity: ActivityStatusEnum) => {
	switch (activity) {
		case ActivityStatusEnum.ONLINE: {
			return "ONLINE";
		}
		case ActivityStatusEnum.DO_NOT_DISTURB: {
			return "DO_NOT_DISTURB";
		}
		case ActivityStatusEnum.AWAY: {
			return "AWAY";
		}
		case ActivityStatusEnum.OFFLINE: {
			return "OFFLINE";
		}
		default: {
			console.error("Switch in activityToString is incomplete");
			return "";
		}
	}
};

export const stringToActivity = (s: string) => {
	switch (s) {
		case "ONLINE": {
			return ActivityStatusEnum.ONLINE;
		}
		case "DO_NOT_DISTURB": {
			return ActivityStatusEnum.DO_NOT_DISTURB;
		}
		case "AWAY": {
			return ActivityStatusEnum.AWAY;
		}
		case "OFFLINE": {
			return ActivityStatusEnum.OFFLINE;
		}
		default: {
			console.error(`Unknown string ${s} called in stringToActivity`);
			return "";
		}
	}
};
