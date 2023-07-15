package me.theophobia.shtipsbackend.service;

import me.theophobia.shtipsbackend.Update;
import me.theophobia.shtipsbackend.repo.UpdateRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdateService {
	private final UserService userService;
	private final UpdateRepo updateRepo;

	public UpdateService(
		UserService userService,
		UpdateRepo updateRepo
	) {
		this.userService = userService;
		this.updateRepo = updateRepo;
	}

	public List<Update> getUpdates(Long userId) {
		return userService.getUser(userId).map(updateRepo::findAllByUser).orElse(List.of());
	}

	public Update save(Update update) {
		return updateRepo.save(update);
	}

	public void deleteAll(Iterable<? extends Update> entities) {
		updateRepo.deleteAll(entities);
	}
}
