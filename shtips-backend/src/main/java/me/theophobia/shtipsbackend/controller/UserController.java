package me.theophobia.shtipsbackend.controller;

import me.theophobia.shtipsbackend.AuthToken;
import me.theophobia.shtipsbackend.User;
import me.theophobia.shtipsbackend.UserLoginRequest;
import me.theophobia.shtipsbackend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepo;

	// User login endpoint
	@PostMapping("/api/user/login")
	public ResponseEntity<String> loginUser(@RequestBody UserLoginRequest request) {
		try {
			User user = null;
			switch (request.getIdentifierType()) {
				case EMAIL -> user = userRepo.findByEmail(request.getIdentifier());
				case USERNAME -> user = userRepo.findByUsername(request.getIdentifier());
			}

			AuthToken authToken = new AuthToken(user.getId());

			return ResponseEntity.ok(authToken.generate());
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
}
