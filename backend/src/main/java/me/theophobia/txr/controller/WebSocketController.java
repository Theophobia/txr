package me.theophobia.txr.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import me.theophobia.txr.activity.ActivityStatus;
import me.theophobia.txr.service.ActivityService;
import me.theophobia.txr.util.LocalDateTimeAdapter;
import me.theophobia.txr.chat.Message;
import me.theophobia.txr.service.AuthService;
import me.theophobia.txr.service.MessageService;
import me.theophobia.txr.service.UserService;
import me.theophobia.txr.user.User;
import me.theophobia.txr.ws.WebSocketStore;
import me.theophobia.txr.ws.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
//		LOGGER.info("id -> {}", id);

		AuthService authService = AuthService.getInstance();
		UserService userService = UserService.getInstance();
		ActivityService activityService = ActivityService.getInstance();
		MessageService messageService = MessageService.getInstance();
		WebSocketStore webSocketStore = WebSocketStore.getInstance();

		try {
			switch (id) {
				case 1 -> {
					Event1 e = GSON.fromJson(data, Event1.class);
					LOGGER.info("event1 = {}", e);
					handleEvent1(e, session, authService, webSocketStore);
				}
				case 11 -> {
					Event11 e = GSON.fromJson(data, Event11.class);
					LOGGER.info("event11 = {}", e);
					handleEvent11(e, authService, userService, messageService, activityService, webSocketStore);
				}
				case 13 -> {
					Event13 e = GSON.fromJson(data, Event13.class);
					LOGGER.info("event13 = {}", e);
					handleEvent13(e, authService, userService, messageService, webSocketStore);
				}
				case 30 -> {
					Event30 e = GSON.fromJson(data, Event30.class);
					LOGGER.info("event30 = {}", e);
					handleEvent30(e, userService, activityService, webSocketStore);
				}
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

	private void handleEvent1(Event1 e, Session session, AuthService authService, WebSocketStore webSocketStore) {
		// Check if token is valid
		if (authService.isInvalidToken(e.getUserId(), e.getToken())) {
			LOGGER.warn("Rejected, invalid token {}", e);
			return;
		}

		// Add to WebSocketStore
		if (webSocketStore.addSession(e.getToken(), e.getSlot(), session, e.getChannels()).isPresent()) {
			LOGGER.info("Accepted {}", e);
		}
		else {
			LOGGER.warn("Rejected, web socket store error {}", e);
			return;
		}
	}

	private void handleEvent11(
		Event11 e,
		AuthService authService,
		UserService userService,
		MessageService messageService,
		ActivityService activityService,
		WebSocketStore webSocketStore
	) {
		// Check if token is valid
		if (authService.isInvalidToken(e.getUserId(), e.getToken())) {
			LOGGER.warn("Rejected, invalid token {}", e);
			return;
		}

		// Get sender from UserService
		Optional<User> optSender = userService.getUser(e.getUserId());
		if (optSender.isEmpty()) {
			LOGGER.warn("Rejected, sender not found {}", e);
			return;
		}
		User sender = optSender.get();

		// Get receiver from UserService
		Optional<User> optReceiver = userService.getUserByUsername(e.getReceiver());
		if (optReceiver.isEmpty()) {
			LOGGER.warn("Rejected, sender not found {}", e);
			return;
		}
		User receiver = optReceiver.get();

		// Check if message is between the same user
		if (Objects.equals(sender.getId(), receiver.getId())) {
			LOGGER.error("Rejected, message between the same user");
			return;
		}

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
		msg = messageService.save(msg);
		LOGGER.info("Accepted, saved message {}", e);

		// Build update to receiver, if they have a websocket connection
		Event10 updateForReceiver = Event10.builder()
			.messageId(msg.getMessageId())
			.sender(msg.getSender().getUsername())
			.timestamp(msg.getTimestamp())
			.type(e.getType())
			.data(e.getData())
			.bonusData(e.getBonusData())
			.build();

		// Send update
		webSocketStore.sendMessage(msg.getReceiver(), "0010", updateForReceiver.json());
		LOGGER.info("Accepted, sent update {}", e);

		// Confirm that message has been successfully sent
		Event12 event12 = Event12.builder()
			.messageId(msg.getMessageId())
			.timestamp(msg.getTimestamp())
			.type(e.getType())
			.data(e.getData())
			.bonusData(e.getBonusData())
			.build();

		LOGGER.info("{}", event12);
		webSocketStore.sendMessage(msg.getSender(), "0012", event12.json());

		// Send activity update
		Event31.Data event31Data = new Event31.Data(msg.getSender().getUsername(), activityService.getActivityStatusOf(msg.getSender()));
		Event31 event31 = new Event31(List.of(event31Data));
		LOGGER.info("{}", event31);
		webSocketStore.sendMessage(msg.getSender(), "0031", event31.json());
		webSocketStore.sendMessage(msg.getReceiver(), "0031", event31.json());
	}

	private void handleEvent13(
		Event13 e,
		AuthService authService,
		UserService userService,
		MessageService messageService,
		WebSocketStore webSocketStore
	) {

		// Check if token is valid
		if (authService.isInvalidToken(e.getUserId(), e.getToken())) {
			LOGGER.warn("Rejected, invalid token {}", e);
			return;
		}

		// Get sender from UserService
		Optional<User> optSender = userService.getUser(e.getUserId());
		if (optSender.isEmpty()) {
			LOGGER.warn("Rejected, sender not found {}", e);
			return;
		}
		User sender = optSender.get();

		// Get receiver from UserService
		Optional<User> optReceiver = userService.getUserByUsername(e.getReceiver());
		if (optReceiver.isEmpty()) {
			LOGGER.warn("Rejected, sender not found {}", e);
			return;
		}
		User receiver = optReceiver.get();

		// Check if message is between the same user
		if (Objects.equals(sender.getId(), receiver.getId())) {
			LOGGER.warn("Rejected, message between the same user");
			return;
		}

		// Fetch messages
		List<Message> messages = messageService.getMessagesBetweenUsers(sender, receiver, e.getTimestamp(), 10);

		// Build event
		Event14 event14 = new Event14(receiver.getUsername(), messages.stream().map(Message::toAnonymousSenderMessage).toList());

		LOGGER.info("{}", event14);
		webSocketStore.sendMessage(sender, "0014", event14.json());
	}

	private void handleEvent30(
		Event30 e,
		UserService userService,
		ActivityService activityService,
		WebSocketStore webSocketStore
	) {
		// Get sender from UserService
		Optional<User> optSender = userService.getUser(e.getUserId());
		if (optSender.isEmpty()) {
			LOGGER.warn("Rejected, sender not found {}", e);
			return;
		}
		User sender = optSender.get();

		List<String> missingUsernames = e.getUsernames();

		Event31 event31 = activityService.getActivityStatus()
			.entrySet().stream()
			.filter(entry -> {
				boolean contains = e.getUsernames().contains(entry.getKey().getUsername());
				if (contains) {
					missingUsernames.remove(entry.getKey().getUsername());
				}
				return contains;
			})
			.map(entry -> new Event31.Data(entry.getKey().getUsername(), entry.getValue()))
			.collect(new Event31());

		missingUsernames.stream().map(s -> new Event31.Data(s, ActivityStatus.OFFLINE)).forEach(event31::addData);

		LOGGER.info("{}", event31);
		webSocketStore.sendMessage(sender, "0031", event31.json());
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
