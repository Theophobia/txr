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
public final class AuthService {

	private final AuthTokenRepo authTokenRepo;
	private final UserRepo userRepo;

	private static AuthService instance = null;
	public static AuthService getInstance() {
		return instance;
	}

	@Autowired
	public AuthService(AuthTokenRepo authTokenRepo, UserRepo userRepo) {
		this.authTokenRepo = authTokenRepo;
		this.userRepo = userRepo;

		// TODO: probably a based hack, we ball
		if (instance == null) {
			instance = this;
		}
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

	public void logoutUser(User user) {
		Optional<AuthToken> optToken = authTokenRepo.findByUserId(user.getId());
		optToken.ifPresent(authTokenRepo::delete);
	}

	public boolean isInvalidToken(long userId, String tokenString) {
		Optional<AuthToken> optToken = authTokenRepo.findByUserId(userId);

		if (optToken.isEmpty()) {
			return true;
		}

		AuthToken a = optToken.get();
//		System.out.println("a = " + a);
//		System.out.println("a.getToken() = " + a.getToken());
//		System.out.println("tokenString = " + tokenString);

		if (!a.getToken().equals(tokenString)) {
			return true;
		}

//		System.out.println("a.isExpired() = " + a.isExpired());
		if (a.isExpired()) {
			return true;
		}

		boolean isInvalid = !AuthTokenGenerator.verify(userId, a.getTimeCreated(), a.getTimeExpires(), a);
//		System.out.println("isInvalid = " + isInvalid);
		if (isInvalid) {
			return true;
		}

		return false;
	}

	public Optional<User> getUserByToken(String token) {
		Optional<AuthToken> optToken = authTokenRepo.findByToken(token);

		if (optToken.isEmpty()) {
			return Optional.empty();
		}

		if (optToken.get().isExpired()) {
			authTokenRepo.delete(optToken.get());
			return Optional.empty();
		}

		return userRepo.findById(optToken.get().getUserId());
	}
}
