package me.theophobia.shtipsbackend.ws;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@ServerEndpoint(value = "/api/ws")
public class WebSocketController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketController.class);

	@OnOpen
	public void onOpen(Session session, EndpointConfig endpointConfig) throws IOException {
		// Get session and WebSocket connection
		session.setMaxIdleTimeout(0);
		LOGGER.info("Get session and WebSocket connection");
	}

	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		// Handle new messages
		LOGGER.info("Handle new messages -> {}", message);

		if (!message.startsWith("token=")) {
			return;
		}

		String token = message.substring("token=".length());
		WebSocketStore webSocketStore = WebSocketStore.getInstance();
//		System.out.println("webSocketStore = " + webSocketStore);

		if (webSocketStore.addSession(token, session).isPresent()) {
			LOGGER.info("Added token session {}", token);
		}
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		// WebSocket connection closes
		LOGGER.info("WebSocket connection closes");

		WebSocketStore webSocketStore = WebSocketStore.getInstance();
		webSocketStore.deleteBySession(session);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
		LOGGER.info("Do error handling here");
	}
}
