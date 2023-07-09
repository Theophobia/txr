export default interface Message {
	senderUsername: string,
	timestamp: string,
	type: "TEXT" | "FILE",
	data: string,
	bonusData: string,
}
