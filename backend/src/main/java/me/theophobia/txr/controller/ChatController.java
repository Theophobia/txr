package me.theophobia.txr.controller;

import me.theophobia.txr.chat.AnonymousSenderMessage;
import me.theophobia.txr.util.Format;
import me.theophobia.txr.ws.WebSocketStore;
import me.theophobia.txr.chat.RecentChat;
import me.theophobia.txr.auth.AuthToken;
import me.theophobia.txr.chat.Message;
import me.theophobia.txr.chat.MessageDataType;
import me.theophobia.txr.service.AuthService;
import me.theophobia.txr.service.MessageService;
import me.theophobia.txr.service.UserService;
import me.theophobia.txr.user.User;
import me.theophobia.txr.util.Tuple4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/chat")
public final class ChatController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

	private final MessageService messageService;
	private final AuthService authService;
	private final UserService userService;

	@Autowired
	public ChatController(
		MessageService messageService,
		AuthService authService,
		UserService userService)
	{
		this.messageService = messageService;
		this.authService = authService;
		this.userService = userService;
	}

	@PostMapping(path = "/message/send")
	public ResponseEntity<?> sendMessage(
		@RequestParam long userId,
		@RequestParam String token,
		@RequestParam String receiver,
		@RequestParam String message
	) {
		token = UriUtils.decode(token, "UTF-8");
		receiver = UriUtils.decode(receiver, "UTF-8");
		message = UriUtils.decode(message, "UTF-8");

		var t = resolve(userId, receiver, token);

		// Check if error occurred
		if (t.getD() != null) {
			return t.getD();
		}

		Message msg = new Message();
		msg.setSender(t.getA());
		msg.setReceiver(t.getB());
		msg.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime());

		msg.setData(message);
		msg.setType(MessageDataType.TEXT);

//		System.out.println("msg = " + msg);
		messageService.save(msg);

		WebSocketStore webSocketStore = WebSocketStore.getInstance();
//		webSocketStore.print();
		webSocketStore.sendMessage(msg.getReceiver(), "0001", msg.toAnonymousSenderMessage().json());

		return ResponseEntity.ok().build();
	}

	@GetMapping(path = "/message/get")
	public ResponseEntity<?> getMessagesWith(
		@RequestParam long userId,
		@RequestParam String token,
		@RequestParam String receiver,
		@RequestParam(defaultValue = "") String before
	) {
		token = UriUtils.decode(token, "UTF-8");
		receiver = UriUtils.decode(receiver, "UTF-8");
		before = UriUtils.decode(before, "UTF-8");
		var t = resolve(userId, receiver, token);

		// Check if error occurred
		if (t.getD() != null) {
			return t.getD();
		}

		// Parse timestamp
		LocalDateTime timeBefore;
		LOGGER.info("Timestamp string is {}", before);
		try {
			if (before.isEmpty()) {
				timeBefore = LocalDateTime.now();
				LOGGER.info("Using LocalDateTime.now() as 'before'");
			}
			else {
				timeBefore = LocalDateTime.from(Format.DATE_TIME_FORMATTER.parse(before));
				LOGGER.info("Parsed timeBefore as {} {}", Format.DATE_TIME_FORMATTER.format(timeBefore), timeBefore);
			}
		}
		catch (Exception e) {
			LOGGER.error("Error parsing timestamp");
			return ResponseEntity.badRequest().build();
		}

		List<AnonymousSenderMessage> messages = messageService.getMessagesBetweenUsers(t.getA(), t.getB(), timeBefore, 10)
			.stream().map(Message::toAnonymousSenderMessage).toList();

		return ResponseEntity.ok(messages);
	}

	@GetMapping(path = "/recent")
	public ResponseEntity<?> getRecentConversations(
		@RequestParam long userId,
		@RequestParam String token
	) {
		token = UriUtils.decode(token, "UTF-8");

		if (authService.isInvalidToken(userId, token)) {
			return ResponseEntity.status(401).body("Invalid auth token");
		}

		List<RecentChat> recentChats = messageService.getRecentChats(userId);

		return ResponseEntity.ok(recentChats);
	}

	private Tuple4<User /*Sender*/, User /*Receiver*/, AuthToken, ResponseEntity<?> /*Error?*/> resolve(long userId, String receiverUsername, String token) {
		final Tuple4<User, User, AuthToken, ResponseEntity<?>> result = new Tuple4<>();

		// Attempt to find sender
		Optional<User> optionalSender = userService.getUser(userId);
		if (optionalSender.isEmpty()) {
			result.setD(ResponseEntity.badRequest().body("Unknown sender"));
			return result;
		}
		User sender = optionalSender.get();
		result.setA(sender);

		// Attempt to find receiver
		Optional<User> optionalReceiver = userService.getUserByUsername(receiverUsername);
		if (optionalReceiver.isEmpty()) {
			result.setD(ResponseEntity.badRequest().body("Unknown receiver"));
			return result;
		}
		User receiver = optionalReceiver.get();
		result.setB(receiver);

		if (authService.isInvalidToken(sender.getId(), token)) {
			result.setD(ResponseEntity.status(401).body("Invalid auth token"));
			return result;
		}

		return result;
	}
}
