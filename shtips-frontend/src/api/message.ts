export default interface Message {
	sender: string,
	timestamp: string,
	type: "TEXT" | "FILE",
	data: string,
	bonusData: string | null,
}
