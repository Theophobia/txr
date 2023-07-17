export interface Event1 {
	userId: number,
	token: string,
}

export interface Event10 {
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
	timestamp: string,
}
