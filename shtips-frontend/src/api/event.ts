import Message from "./message";
import {ActivityStatusEnum} from "./activityStatus";

export interface Event1 {
	userId: number,
	slot: number,
	token: string,
	channels: string[],
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
//
// export interface Event20 {
// 	other_person_username: string,
// 	timestamp: string,
// 	type: "TEXT" | "FILE",
// 	data: string,
// 	bonusData: string | null,
// }
//

export interface Event30 {
	userId: number,
	usernames: string[],
}

export interface Event31Data {
	username: string,
	activity: ActivityStatusEnum,
}

export interface Event31 {
	data: Event31Data[],
}
