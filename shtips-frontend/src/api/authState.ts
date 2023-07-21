import UserData from "./userData";

export interface AuthState {
	isLoggedIn: boolean,
	userData: UserData | null,
	token: string | null,
}
