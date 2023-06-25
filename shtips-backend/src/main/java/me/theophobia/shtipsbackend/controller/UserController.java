package me.theophobia.shtipsbackend.controller;

import me.theophobia.shtipsbackend.user.TokenValidityRequest;
import me.theophobia.shtipsbackend.auth.AuthToken;
import me.theophobia.shtipsbackend.auth.AuthTokenGenerator;
import me.theophobia.shtipsbackend.repo.AuthTokenRepo;
import me.theophobia.shtipsbackend.repo.UserRepo;
import me.theophobia.shtipsbackend.user.User;
import me.theophobia.shtipsbackend.user.UserLoginRequest;
import me.theophobia.shtipsbackend.user.UserRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/user")
public final class UserController {

	@Autowired
	private AuthTokenRepo authTokenRepo;

	@Autowired
	private UserRepo userRepo;

	// User login endpoint
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody UserLoginRequest request) {
		Optional<User> optionalUser = Optional.empty();
		switch (request.identifierType()) {
			case EMAIL -> optionalUser = userRepo.findByEmail(request.identifier());
			case USERNAME -> optionalUser = userRepo.findByUsername(request.identifier());
		}

		if (optionalUser.isEmpty()) {
			return ResponseEntity.badRequest().body("No such user");
		}

		User user = optionalUser.get();

		if (!user.getPassword().equals(request.password())) {
			return ResponseEntity.badRequest().body("Wrong password");
		}

		// Remove existing auth tokens for user
		authTokenRepo.deleteById(user.getId());

		// Generate new token
		AuthToken authToken = AuthTokenGenerator.generate(user.getId());

		if (authToken == null) {
			return ResponseEntity.internalServerError().build();
		}

		authTokenRepo.save(authToken);

		return ResponseEntity.ok(authToken.getToken());
	}

	// User register endpoint
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest request) {
		User user = new User();

		user.setEmail(request.email());
		user.setUsername(request.username());
		user.setPassword(request.password());

		if (!user.hasStrongPassword()) {
			return ResponseEntity.badRequest().body("Weak password");
		}

		try {
			userRepo.save(user);
			return ResponseEntity.ok().build();
		}
		catch (final Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/valid")
	public ResponseEntity<?> isValidToken(@RequestBody TokenValidityRequest request) {
		Optional<AuthToken> optionalLocalToken = authTokenRepo.findByUserId(request.userId());
		if (optionalLocalToken.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		AuthToken localToken = optionalLocalToken.get();

		if (!localToken.getToken().equals(request.token())) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok().build();
	}
}
