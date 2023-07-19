import Message from "./message";

export interface Event1 {
	userId: number,
	token: string,
}

export interface Event10 {
	messageId: number,
	sender: string,
	timestamp: string,
	type: "TEXT" | "FILE",
	data: string,
	bonusData: string,
}

export interface Event11 {
	userId: number,
	token: string,
	receiver: string,
	timestamp: string,
	type: "TEXT" | "FILE",
	data: string,
	bonusData: string | null,
}

export interface Event12 {
	messageId: number,
	timestamp: string,
	type: "TEXT" | "FILE",
	data: string,
	bonusData: string,
}

export interface Event13 {
	userId: number,
	token: string,
	receiver: string,
	timestamp: string,
}

export interface Event14 {
	sender: string,
	messages: Message[],
}
