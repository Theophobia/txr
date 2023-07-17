package me.theophobia.shtipsbackend.ws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import me.theophobia.shtipsbackend.LocalDateTimeAdapter;
import me.theophobia.shtipsbackend.chat.Message;
import me.theophobia.shtipsbackend.service.AuthService;
import me.theophobia.shtipsbackend.service.MessageService;
import me.theophobia.shtipsbackend.service.UserService;
import me.theophobia.shtipsbackend.user.User;
import me.theophobia.shtipsbackend.ws.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@ServerEndpoint(value = "/api/ws")
public class WebSocketController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketController.class);
	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();

	@OnOpen
	public void onOpen(Session session, EndpointConfig endpointConfig) throws IOException {
		// Get session and WebSocket connection
		session.setMaxIdleTimeout(0);
		LOGGER.info("Get session and WebSocket connection");
	}

	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		// Handle new messages
		LOGGER.info("WebSocket message -> {}", message);

		String idString = message.substring(0, 4);
		String data = message.substring(4);
		int id = Integer.parseInt(idString);
		LOGGER.info("id -> {}", id);

		try {
			switch (id) {
				case 1 -> {
					Event1 e = GSON.fromJson(data, Event1.class);
					LOGGER.info("event1 = {}", e);

					AuthService authService = AuthService.getInstance();

					// Check if token is valid
					if (authService.isInvalidToken(e.getUserId(), e.getToken())) {
						LOGGER.error("Rejected, invalid token {}", e);
					}

					// Add to WebSocketStore
					WebSocketStore webSocketStore = WebSocketStore.getInstance();
					if (webSocketStore.addSession(e.getToken(), session).isPresent()) {
						LOGGER.info("Accepted {}", e);
					}
					else {
						LOGGER.error("Rejected, web socket store error {}", e);
					}
				}
				// We send this data, not receive, no need to check
//				case 10 -> {
//					Event10 e = GSON.fromJson(data, Event10.class);
//				}
				case 11 -> {
					Event11 e = GSON.fromJson(data, Event11.class);
					LOGGER.info("event11 = {}", e);

					AuthService authService = AuthService.getInstance();

					// Check if token is valid
					if (authService.isInvalidToken(e.getUserId(), e.getToken())) {
						LOGGER.error("Rejected, invalid token {}", e);
					}

					UserService userService = UserService.getInstance();

					// Get sender from UserService
					Optional<User> optSender = userService.getUser(e.getUserId());
					if (optSender.isEmpty()) {
						LOGGER.error("Rejected, sender not found {}", e);
					}
					User sender = optSender.get();

					// Get receiver from UserService
					Optional<User> optReceiver = userService.getUserByUsername(e.getReceiver());
					if (optReceiver.isEmpty()) {
						LOGGER.error("Rejected, sender not found {}", e);
					}
					User receiver = optReceiver.get();

					// Build message
					Message msg = Message.builder()
						.sender(sender)
						.receiver(receiver)
						.timestamp(e.getTimestamp()) // TODO: check if sent timestamp can be accurate (sent less than 10 secs ago)
						.type(e.getType())
						.data(e.getData())
						.bonusData(e.getBonusData())
						.build();

					// Save to database
					MessageService messageService = MessageService.getInstance();
					messageService.save(msg);
					LOGGER.info("Accepted, saved message {}", e);

					// Build update to receiver, if they have a websocket connection
					WebSocketStore webSocketStore = WebSocketStore.getInstance();
					if (webSocketStore.getUserSessionMap().containsKey(msg.getReceiver())) {
						Event10 updateForReceiver = Event10.builder()
							.sender(msg.getSender().getUsername())
							.timestamp(msg.getTimestamp())
							.type(e.getType())
							.data(e.getData())
							.bonusData(e.getBonusData())
							.build();

						// Send update
						webSocketStore.sendMessage(msg.getReceiver(), "0010" + updateForReceiver.json());
						LOGGER.info("Accepted, sent update {}", e);
					}
					else {
						LOGGER.info("Accepted, did not send update {}", e);
					}

					// Confirm that message has been successfully sent
					Event12 event12 = new Event12(msg.getTimestamp());
					webSocketStore.sendMessage(msg.getSender(), "0012" + event12.json());

				}
				case 13 -> {
					Event13 e = GSON.fromJson(data, Event13.class);
				}
				// We send this data, not receive, no need to check
//				case 14 -> {
//					Event14 e = GSON.fromJson(data, Event14.class);
//				}
				default -> {
					LOGGER.error("Could not match ID, do we have different API versions?");
				}
			}
		}
		catch (Exception e) {
			LOGGER.error("Error parsing event, do we have different API versions?");
			e.printStackTrace();
		}

//		String token = message.substring("token=".length());
//		WebSocketStore webSocketStore = WebSocketStore.getInstance();
//		System.out.println("webSocketStore = " + webSocketStore);

//		if (webSocketStore.addSession(token, session).isPresent()) {
//			LOGGER.info("Added session {}", token);
//		}
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
		throwable.printStackTrace();
	}
}
