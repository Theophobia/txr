package me.theophobia.shtipsbackend.controller;

import me.theophobia.shtipsbackend.auth.AuthToken;
import me.theophobia.shtipsbackend.auth.AuthTokenGenerator;
import me.theophobia.shtipsbackend.message.Message;
import me.theophobia.shtipsbackend.message.MessageDataType;
import me.theophobia.shtipsbackend.repo.AuthTokenRepo;
import me.theophobia.shtipsbackend.repo.MessageRepo;
import me.theophobia.shtipsbackend.repo.UserRepo;
import me.theophobia.shtipsbackend.service.MessageService;
import me.theophobia.shtipsbackend.user.User;
import me.theophobia.shtipsbackend.util.Tuple4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/message")
public final class MessageController {

	private final UserRepo userRepo;
	private final AuthTokenRepo authTokenRepo;
	private final MessageRepo messageRepo;
	private final MessageService messageService;

	@Autowired
	public MessageController(UserRepo userRepo, AuthTokenRepo authTokenRepo, MessageRepo messageRepo, MessageService messageService) {
		this.userRepo = userRepo;
		this.authTokenRepo = authTokenRepo;
		this.messageRepo = messageRepo;
		this.messageService = messageService;
	}

	@PostMapping(path = "/send")
	public ResponseEntity<?> sendMessage(
		@RequestParam long userId,
		@RequestParam String token,
		@RequestParam String receiver,
		@RequestParam String message
	) {

		var t = resolve(userId, receiver, token);

		// Check if error occurred
		if (t.d() != null) {
			return t.d();
		}

		Message msg = new Message();
		msg.setSender(t.a());
		msg.setReceiver(t.b());
		msg.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime());

		msg.setData(message);
		msg.setType(MessageDataType.TEXT);

		messageRepo.save(msg);

		return ResponseEntity.ok().build();
	}

	@PostMapping(path = "/latest")
	public ResponseEntity<?> getLatestMessagesWith(
		@RequestParam long userId,
		@RequestParam String token,
		@RequestParam String receiver,
		@RequestParam int pageNumber,
		@RequestParam int pageSize
	) {
		var t = resolve(userId, receiver, token);

		// Check if error occurred
		if (t.d() != null) {
			return t.d();
		}

		Pageable pageable = PageRequest.of(pageNumber, pageSize /*, Sort.by("timestamp").descending()*/);

		return ResponseEntity.ok(messageService.getMessagesBetweenUsers(t.a(), t.b(), pageable).map(Message::toAnonymousMessage).toList());
	}

	private Tuple4<User /*Sender*/, User /*Receiver*/, AuthToken, ResponseEntity<?> /*Error?*/> resolve(long userId, String receiverUsername, String token) {
		final Tuple4<User, User, AuthToken, ResponseEntity<?>> result = new Tuple4<>();

		// Attempt to find receiver
		Optional<User> optionalReceiver = userRepo.findByUsername(receiverUsername);
		if (optionalReceiver.isEmpty()) {
			result.setD(ResponseEntity.badRequest().body("Unknown receiver"));
			return result;
		}
		User receiver = optionalReceiver.get();
		result.setB(receiver);

		// Attempt to find sender
		Optional<User> optionalSender = userRepo.findById(userId);
		if (optionalSender.isEmpty()) {
			result.setD(ResponseEntity.badRequest().body("Unknown sender"));
			return result;
		}
		User sender = optionalSender.get();
		result.setA(sender);

		// Check if auth token exists
		Optional<AuthToken> optionalLocalToken = authTokenRepo.findByUserId(userId);
		if (optionalLocalToken.isEmpty()) {
			result.setD(ResponseEntity.badRequest().body("Expired auth token"));
			return result;
		}
		AuthToken localToken = optionalLocalToken.get();
		result.setC(localToken);

		// Check if token is invalid
		boolean isInvalid = !AuthTokenGenerator.verify(sender.getId(), localToken.getTimeCreated(), localToken.getTimeExpires(), localToken);
		if (isInvalid || !localToken.getToken().equals(token)) {
			result.setD(ResponseEntity.badRequest().body("Bad auth token"));
			return result;
		}

		// Check if token is expired
		boolean isExpired = System.currentTimeMillis() > localToken.getTimeExpires();
		if (isExpired) {
			authTokenRepo.delete(localToken);
			result.setD(ResponseEntity.badRequest().body("Expired auth token"));
			return result;
		}

		return result;
	}
}
