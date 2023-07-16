package me.theophobia.shtipsbackend.service;

import me.theophobia.shtipsbackend.update.NewMessageUpdate;
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

	public List<NewMessageUpdate> getNewMessageUpdates(Long userId) {
		return userService.getUser(userId).map(updateRepo::findAllByReceiver).orElse(List.of());
	}

	public NewMessageUpdate save(NewMessageUpdate newMessageUpdate) {
		return updateRepo.save(newMessageUpdate);
	}

	public void deleteAll(Iterable<? extends NewMessageUpdate> entities) {
		updateRepo.deleteAll(entities);
	}
}
