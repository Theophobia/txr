namespace Query {
	const API_URL = "http://localhost:8080";

	export async function apiChatMessageGet(userId: number, token: string, username: string, pageNumber: number, pageSize: number): Promise<Response | null> {
		if (token.length === 0 || username.length === 0) {
			return null;
		}

		return await fetch(`${API_URL}/api/chat/message/get
			?userId=${encodeURIComponent(userId)}
			&token=${encodeURIComponent(token)}
			&receiver=${encodeURIComponent(username)}
			&pageNumber=${pageNumber}
			&pageSize=${pageSize}
			`, {
			method: "GET"
		});
	}

	export async function apiChatRecent(userId: number, token: string): Promise<Response | null> {
		if (token.length === 0) {
			return null;
		}

		return await fetch(`${API_URL}/api/chat/recent
			?userId=${encodeURIComponent(userId)}
			&token=${encodeURIComponent(token)}
			`, {
			method: "GET"
		});
	}

	export async function apiUserLogin(usernameOrEmail: string, password: string): Promise<Response | null> {
		if (usernameOrEmail.length === 0 || password.length === 0) {
			return null;
		}

		return await fetch(`${API_URL}/api/user/login
			?usernameOrEmail=${encodeURIComponent(usernameOrEmail)}
			&password=${encodeURIComponent(password)}
			`, {
			method: "POST"
		});
	}

	export async function apiUserInfo(token: string): Promise<Response | null> {
		if (token.length === 0) {
			return null;
		}

		return await fetch(`${API_URL}/api/user/info
			?token=${encodeURIComponent(token)}`, {
			method: "GET"
		});
	}
}
