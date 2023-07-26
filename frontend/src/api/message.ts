export enum MessageStatus {
	SENDING, SENT, DELIVERED, SEEN
}

export default interface Message {
	messageId: number,
	status: MessageStatus,
	sender: string,
	timestamp: string,
	type: "TEXT" | "FILE",
	data: string,
	bonusData: string | null,
}
