package me.theophobia.txr.ws;

import jakarta.websocket.Session;
import me.theophobia.txr.service.AuthService;
import me.theophobia.txr.service.UserService;
import me.theophobia.txr.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class WebSocketStore {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketStore.class);
	private static final int MAX_SLOTS = 2;

	private final UserSessionMap userSessionMap = new UserSessionMap();

	private final UserService userService;
	private final AuthService authService;

	private static WebSocketStore instance = null;
	public static WebSocketStore getInstance() {
		return instance;
	}

	@Autowired
	public WebSocketStore(
		UserService userService,
		AuthService authService
	) {
		this.userService = userService;
		this.authService = authService;

		// TODO: probably a based hack, we ball
		if (instance == null) {
			instance = this;
		}
	}

	public Optional<User> addSession(String token, int slot, Session session, List<String> channels) {
		if (slot >= MAX_SLOTS) {
			LOGGER.error("slot is {} but MAX_SLOTS is {}", slot, MAX_SLOTS);
			return Optional.empty();
		}

		Optional<User> optUser = authService.getUserByToken(token);
		if (optUser.isEmpty()) {
			return Optional.empty();
		}
		User user = optUser.get();

		if (authService.isInvalidToken(user.getId(), token)) {
			return Optional.empty();
		}

		userSessionMap.addSession(user, slot, session, channels);

		return Optional.of(user);
	}

	public boolean sendMessage(User user, String channel, String message) {
		if (channel.length() != 4) {
			throw new IllegalArgumentException("channel.length() != 4");
		}
		return userSessionMap.sendMessage(user, channel, message);
	}

	public boolean sendMessage(long userId, String channel, String message) {
		Optional<User> optUser = userService.getUser(userId);
		if (optUser.isEmpty()) {
			return false;
		}
		User user = optUser.get();
		return sendMessage(user, channel, message);
	}

	public void deleteBySession(Session session) {
		userSessionMap.deleteSession(session);
	}

	public void print() {
		System.out.println("userSessionMap = " + userSessionMap);
	}
}
