package me.theophobia.txr.controller;

import me.theophobia.txr.service.AuthService;
import me.theophobia.txr.service.UserAvatarService;
import me.theophobia.txr.service.UserService;
import me.theophobia.txr.user.*;
import me.theophobia.txr.auth.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/user")
public final class UserController {

	private static final int LEVENSHTEIN_DIST = 5;
	private static final int SEARCH_USERS = 10;

	private final AuthService authService;
	private final UserService userService;
	private final UserAvatarService userAvatarService;

	@Autowired
	public UserController(
		AuthService authService,
		UserService userService,
		UserAvatarService userAvatarService
	) {
		this.authService = authService;
		this.userService = userService;
		this.userAvatarService = userAvatarService;
	}

	// TODO: Move to auth controller
	// User login endpoint
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(
		@RequestParam String usernameOrEmail,
		@RequestParam String password
	) {
		Optional<AuthToken> optToken = authService.loginUser(usernameOrEmail, password);

		if (optToken.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(optToken.get().getToken());
	}

	// TODO: Move to auth controller
	// User login endpoint
	@PostMapping("/logout")
	public ResponseEntity<String> logoutUser(
		@RequestParam String token
	) {
		Optional<User> optUser = authService.getUserByToken(token);

		if (optUser.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		authService.logoutUser(optUser.get());
		return ResponseEntity.ok().build();
	}

	// User register endpoint
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(
		@RequestParam String email,
		@RequestParam String username,
		@RequestParam String password
	) {
		Optional<User> optUser = userService.registerUser(email, username, password);

		if (optUser.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		Optional<AuthToken> optToken = authService.loginUser(email, password);

		if (optToken.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok().body(optToken.get().getToken());
	}

	// TODO: Move to auth controller
	@GetMapping("/valid")
	public ResponseEntity<?> isValidToken(
		@RequestParam String token
	) {
		Optional<User> optUser = authService.getUserByToken(token);
		if (optUser.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok().build();
	}

	@GetMapping("/info")
	public ResponseEntity<?> getUserInfo(
		@RequestParam String token
	) {
		Optional<User> optUser = authService.getUserByToken(token);
		if (optUser.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(optUser.get().toPasswordlessUser());
	}

	@GetMapping("/search")
	public ResponseEntity<?> search(
		@RequestParam Long userId,
		@RequestParam String token,
		@RequestParam String search
	) {
		if (authService.isInvalidToken(userId, token)) {
			return ResponseEntity.badRequest().build();
		}

		Optional<User> optUser = authService.getUserByToken(token);
		if (optUser.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		User user = optUser.get();

		List<String> usernames = userService.searchSimilar(search, LEVENSHTEIN_DIST, SEARCH_USERS)
			.stream().map(User::getUsername).filter(s -> !s.equals(user.getUsername())).toList();

		return ResponseEntity.ok(usernames);
	}

	@GetMapping("/avatar")
	public ResponseEntity<?> getAvatar(
		@RequestParam String username,
		@RequestParam Optional<Integer> size
	) {
		Optional<User> optUser = userService.getUserByUsername(username);
		if (optUser.isEmpty()) {
			return ResponseEntity.badRequest().body("Could not find specified user");
		}
		User user = optUser.get();

		Optional<UserAvatar> optAvatar = userAvatarService.getUserAvatar(user);
		if (optAvatar.isEmpty()) {
			UserAvatar avatar = new UserAvatar();
			avatar.setUser(user);
			optAvatar = Optional.of(userAvatarService.save(avatar));
		}

		UserAvatar userAvatar = optAvatar.get();

		// Set the appropriate headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);

		if (size.isPresent() && size.get() != 36) {
			try {
				BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(userAvatar.getImageData()));
				bufferedImage = UserAvatar.resize(bufferedImage, size.get(), size.get());
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, "png", baos);

				return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return ResponseEntity.ok().headers(headers).body(userAvatar.getImageData36());
	}
}
