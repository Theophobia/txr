export enum UpdateType {
	NEW_MESSAGE
}

export class Update {
	public static toUpdateType(value: string): UpdateType | null {
		if (Object.values(UpdateType).includes(value as UpdateType)) {
			return value as UpdateType;
		}

		return null;
	}
}
