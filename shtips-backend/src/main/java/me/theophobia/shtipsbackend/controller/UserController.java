package me.theophobia.shtipsbackend.controller;

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

import java.util.Objects;

@RestController
public class UserController {

	@Autowired
	private AuthTokenRepo authTokenRepo;

	@Autowired
	private UserRepo userRepo;

	// User login endpoint
	@PostMapping("/api/user/login")
	public ResponseEntity<String> loginUser(@RequestBody UserLoginRequest request) {
		User user = null;
		switch (request.identifierType()) {
			case EMAIL -> user = userRepo.findByEmail(request.identifier());
			case USERNAME -> user = userRepo.findByUsername(request.identifier());
		}

		if (user == null) {
			return ResponseEntity.badRequest().body("No such user");
		}

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
	@PostMapping("/api/user/register")
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
}
