package me.theophobia.shtipsbackend.ws;

import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;
import me.theophobia.shtipsbackend.service.AuthService;
import me.theophobia.shtipsbackend.service.UserService;
import me.theophobia.shtipsbackend.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@Component
public class WebSocketStore {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketStore.class);

	private final Map<User, Session> userSessionMap = new HashMap<>();

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

	public Map<User, Session> getUserSessionMap() {
		return userSessionMap;
	}

	public Optional<User> addSession(String token, Session session) {
		Optional<User> optUser = authService.getUserByToken(token);
		if (optUser.isEmpty()) {
			return Optional.empty();
		}
		User user = optUser.get();

		if (authService.isInvalidToken(user.getId(), token)) {
			return Optional.empty();
		}

		if (userSessionMap.containsKey(user)) {
			Session s = userSessionMap.get(user);
			try {
				s.close(new CloseReason(CloseReason.CloseCodes.NO_STATUS_CODE, "Multiple connections"));
			}
			catch (IOException ignored) {}
		}

		userSessionMap.put(user, session);
		return Optional.of(user);
	}

	public boolean sendMessage(User user, String message) {
		LOGGER.info(user.toString());
		userSessionMap.forEach((key, value) -> LOGGER.info(key.toString()));

		if (!userSessionMap.containsKey(user)) {
			LOGGER.info("containsKey return false");
			return false;
		}

		Session s = userSessionMap.get(user);
		try {
			s.getBasicRemote().sendText(message);
			LOGGER.info("Found user {} and sending str {}", user.getUsername(), message);
			return true;
		}
		catch (Exception e) {
			LOGGER.info("catch return false");
			return false;
		}
	}

	public boolean sendMessage(User user, Object obj) {
		return sendMessage(user, obj.toString());
	}

	public boolean sendMessage(long userId, String message) {
		Optional<User> optUser = userService.getUser(userId);
		if (optUser.isEmpty()) {
			return false;
		}
		User user = optUser.get();
		return sendMessage(user, message);
	}

	public void deleteBySession(Session session) {
		User user = null;
		Iterator<Map.Entry<User, Session>> iter = userSessionMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<User, Session> entry = iter.next();
			if (entry.getValue().equals(session)) {
				iter.remove();
				break;
			}
		}
	}
}
