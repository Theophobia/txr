package me.theophobia.shtipsbackend.service;

import me.theophobia.shtipsbackend.repo.UserAvatarRepo;
import me.theophobia.shtipsbackend.repo.UserRepo;
import me.theophobia.shtipsbackend.user.User;
import me.theophobia.shtipsbackend.user.UserAvatar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public final class UserAvatarService {
	private final UserAvatarRepo userAvatarRepo;
	private final UserRepo userRepo;

	@Autowired
	public UserAvatarService(UserAvatarRepo userAvatarRepo, UserRepo userRepo) {
		this.userAvatarRepo = userAvatarRepo;
		this.userRepo = userRepo;
	}

	public Optional<UserAvatar> getUserAvatarByUserId(long userId) {
		return userAvatarRepo.findByUser_Id(userId);
	}

	public Optional<UserAvatar> getUserAvatarByUsername(String username) {
		Optional<User> optUser = userRepo.findByUsername(username);
		if (optUser.isPresent()) {
			return userAvatarRepo.findByUser(optUser.get());
		}
		return Optional.empty();
	}

	public Optional<UserAvatar> getUserAvatar(User user) {
		return userAvatarRepo.findByUser(user);
	}

	public UserAvatar save(UserAvatar userAvatar) {
		Optional<UserAvatar> optAvatar = getUserAvatarByUserId(userAvatar.getUser().getId());
		optAvatar.ifPresent(userAvatarRepo::delete);
		return userAvatarRepo.save(userAvatar);
	}
}
