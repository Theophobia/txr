import UserData from "../api/userData";

export interface AuthState {
	isLoggedIn: boolean,
	userData: UserData | null,
	token: string | null,
}
