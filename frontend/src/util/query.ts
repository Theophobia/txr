const API_URL = "http://localhost:8080";

export async function apiChatMessageGet(userId: number, token: string, receiver: string, before: string): Promise<Response | null> {
	if (token.length === 0 || receiver.length === 0) {
		return null;
	}

	return await fetch(`${API_URL}/api/chat/message/get
		?userId=${encodeURIComponent(userId)}
		&token=${encodeURIComponent(token)}
		&receiver=${encodeURIComponent(receiver)}
		&before=${encodeURIComponent(before)}
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

export async function apiUserRegister(email: string, username: string, password: string): Promise<Response | null> {
	if (email.length === 0 || username.length === 0 || password.length === 0) {
		return null;
	}

	return await fetch(`${API_URL}/api/user/register
		?email=${encodeURIComponent(email)}
		&username=${encodeURIComponent(username)}
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

export async function apiAvatar(username: number): Promise<Response | null> {
	return await fetch(`${API_URL}/api/test/getAvatar
		?username=${encodeURIComponent(username)}`, {
		method: "GET"
	});
}

// export async function apiChatMessageSend(userId: number, token: string, receiver: string, message: string): Promise<Response | null> {
// 	if (token.length === 0 || receiver.length === 0 || message.length === 0) {
// 		return null;
// 	}
//
// 	return await fetch(`${API_URL}/api/chat/message/send
// 		?userId=${encodeURIComponent(userId)}
// 		&token=${encodeURIComponent(token)}
// 		&receiver=${encodeURIComponent(receiver)}
// 		&message=${encodeURIComponent(message)}
// 		`, {
// 		method: "POST"
// 	});
// }

export async function apiUserSearch(userId: number, token: string, search: string): Promise<Response | null> {
	if (search.length === 0 || token.length === 0) {
		return null;
	}

	return await fetch(`${API_URL}/api/user/search
		?userId=${encodeURIComponent(userId)}
		&token=${encodeURIComponent(token)}
		&search=${encodeURIComponent(search)}
		`, {
		method: "GET"
	});
}
