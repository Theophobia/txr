package me.theophobia.shtipsbackend.service;

import me.theophobia.shtipsbackend.repo.UserRepo;
import me.theophobia.shtipsbackend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

	private final UserRepo userRepo;

	@Autowired
	public UserService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	public Optional<User> registerUser(String email, String username, String password) {
		User user = new User(email, username, password);

		if (!user.hasStrongPassword()) {
			System.out.println("ERR 1");
			return Optional.empty();
		}

		try {
			userRepo.save(user);
		}
		catch (Exception e) {
			System.out.println("ERR 2");
			return Optional.empty();
		}

		return Optional.of(user);
	}

	public Optional<User> getUser(long userId) {
		return userRepo.findById(userId);
	}
}
