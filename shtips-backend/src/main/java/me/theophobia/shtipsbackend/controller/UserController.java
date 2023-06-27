package me.theophobia.shtipsbackend.controller;

import me.theophobia.shtipsbackend.service.AuthService;
import me.theophobia.shtipsbackend.service.UserService;
import me.theophobia.shtipsbackend.user.*;
import me.theophobia.shtipsbackend.auth.AuthToken;
import me.theophobia.shtipsbackend.auth.AuthTokenGenerator;
import me.theophobia.shtipsbackend.repo.AuthTokenRepo;
import me.theophobia.shtipsbackend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/user")
public final class UserController {

	private final AuthService authService;
	private final UserService userService;

	@Autowired
	public UserController(AuthService authService, UserService userService) {
		this.authService = authService;
		this.userService = userService;
	}

	// User login endpoint
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(
		@RequestParam String usernameOrPassword,
		@RequestParam String password
	) {
		Optional<AuthToken> optToken = authService.loginUser(usernameOrPassword, password);

		if (optToken.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(optToken.get().getToken());
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

		return ResponseEntity.ok().body(optUser.get());
	}

	@GetMapping("/valid")
	public ResponseEntity<?> isValidToken(
		@RequestParam long userId,
		@RequestParam String token
	) {
		boolean isValid = authService.isValidToken(userId, token);

		if (!isValid) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok().build();
	}

	@GetMapping("/info")
	public ResponseEntity<?> getUserInfo(
		@RequestParam long userId,
		@RequestParam String token
	) {
		boolean isValid = authService.isValidToken(userId, token);

		if (!isValid) {
			return ResponseEntity.badRequest().build();
		}

		// Token wouldn't be valid if user didn't exist
		//noinspection OptionalGetWithoutIsPresent
		return ResponseEntity.ok(userService.getUser(userId).get().toPasswordlessUser());
	}
}
