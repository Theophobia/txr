package me.theophobia.shtipsbackend.service;

import me.theophobia.shtipsbackend.repo.UserAvatarRepo;
import me.theophobia.shtipsbackend.repo.UserRepo;
import me.theophobia.shtipsbackend.user.User;
import me.theophobia.shtipsbackend.user.UserAvatar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public final class UserService {

	private final UserRepo userRepo;
	private final UserAvatarRepo userAvatarRepo;

	@Autowired
	public UserService(UserRepo userRepo, UserAvatarRepo userAvatarRepo) {
		this.userRepo = userRepo;
		this.userAvatarRepo = userAvatarRepo;
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

		UserAvatar userAvatar = new UserAvatar();
		userAvatar.setUser(user);
		try {
			userAvatarRepo.save(userAvatar);
		}
		catch (Exception e) {
			System.out.println("ERR 3");
			return Optional.empty();
		}

		return Optional.of(user);
	}

	public Optional<User> getUser(long userId) {
		return userRepo.findById(userId);
	}

	public Optional<User> getUserByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}
}
