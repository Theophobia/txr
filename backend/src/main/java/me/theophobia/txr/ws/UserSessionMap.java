package me.theophobia.txr.ws;

import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;
import me.theophobia.txr.user.User;
import me.theophobia.txr.util.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class UserSessionMap {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserSessionMap.class);
	private static final int MAX_SLOTS = 3;

	private final Map<User, List<Tuple2<Session, List<String>>>> userSessionMap = new HashMap<>();

	public Map<User, List<Tuple2<Session, List<String>>>> getUserSessionMap() {
		return userSessionMap;
	}

	public boolean containsKey(User user) {
		return userSessionMap.containsKey(user);
	}

	public void addSession(User user, int slot, Session session, List<String> channels) {
		if (slot >= MAX_SLOTS) {
			LOGGER.error("slot is {} but MAX_SLOTS is {}", slot, MAX_SLOTS);
		}

		if (!userSessionMap.containsKey(user)) {
			userSessionMap.put(user, new ArrayList<>(Collections.nCopies(MAX_SLOTS, null)));
		}

		userSessionMap.get(user).set(slot, new Tuple2<>(session, channels));
	}

	public boolean sendMessage(User user, String channel, String message) {
		if (!userSessionMap.containsKey(user)) {
			return false;
		}

		List<Tuple2<Session, List<String>>> tuples = userSessionMap.get(user);
//		System.out.println("userSessionMap = " + userSessionMap);

		boolean hasSent = false;
		for (int i = 0; i < tuples.size(); i++) {
			Tuple2<Session, List<String>> t = tuples.get(i);
//			System.out.println("t = " + t);

			if (t == null) {
				continue;
			}

			if (!t.getB().contains(channel)) {
				continue;
			}

			Session s = t.getA();
			if (s == null) {
				continue;
			}

			try {
				s.getBasicRemote().sendText(channel + message);
				LOGGER.info("Found slot {} user {} and sending str {}", i, user.getUsername(), message);
				hasSent = true;
			}
			catch (Exception e) {
				LOGGER.info("catch return false");
			}
		}
		return hasSent;
	}

	public void deleteSession(Session session) {
		if (session == null) {
			return;
		}

		for (List<Tuple2<Session, List<String>>> tuples : userSessionMap.values()) {
			final int size = tuples.size();
			for (int i = 0; i < size; i++) {
				var curr = tuples.get(i);

				if (curr != null && curr.getA() != null && curr.getA().equals(session)) {
					try {
						curr.getA().close(new CloseReason(CloseReason.CloseCodes.NO_STATUS_CODE, "deleteSession"));
					}
					catch (IOException ignored) {}
					curr.setA(null);
					tuples.set(i, curr);
				}
			}
		}
	}
}
