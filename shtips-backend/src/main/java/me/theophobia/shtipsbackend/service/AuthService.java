package me.theophobia.shtipsbackend.service;

import me.theophobia.shtipsbackend.auth.AuthToken;
import me.theophobia.shtipsbackend.auth.AuthTokenGenerator;
import me.theophobia.shtipsbackend.repo.AuthTokenRepo;
import me.theophobia.shtipsbackend.repo.UserRepo;
import me.theophobia.shtipsbackend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

	private final AuthTokenRepo authTokenRepo;
	private final UserRepo userRepo;

	@Autowired
	public AuthService(AuthTokenRepo authTokenRepo, UserRepo userRepo) {
		this.authTokenRepo = authTokenRepo;
		this.userRepo = userRepo;
	}

	public Optional<AuthToken> loginUser(String usernameOrEmail, String password) {
		Optional<User> optUser;
		if (usernameOrEmail.contains("@")) {
			optUser = userRepo.findByEmail(usernameOrEmail);
		}
		else {
			optUser = userRepo.findByUsername(usernameOrEmail);
		}

		if (optUser.isEmpty()) {
			return Optional.empty();
		}

		User user = optUser.get();
		if (!user.passwordMatches(password)) {
			return Optional.empty();
		}

		Optional<AuthToken> optToken = Optional.ofNullable(AuthTokenGenerator.generate(user.getId()));
		if (optToken.isEmpty()) {
			return Optional.empty();
		}

		authTokenRepo.save(optToken.get());

		return optToken;
	}

	public boolean isValidToken(long userId, String tokenString) {
		Optional<AuthToken> optToken = authTokenRepo.findByUserId(userId);

		if (optToken.isEmpty()) {
			return false;
		}

		AuthToken a = optToken.get();

		if (a.getToken().equals(tokenString)) {
			return false;
		}

		return true;
	}
}
