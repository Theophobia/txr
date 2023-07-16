package me.theophobia.shtipsbackend.controller;

import me.theophobia.shtipsbackend.service.AuthService;
import me.theophobia.shtipsbackend.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/update")
public class UpdateController {

	private final AuthService authService;
	private final UpdateService updateService;

	@Autowired
	public UpdateController(AuthService authService, UpdateService updateService) {
		this.authService = authService;
		this.updateService = updateService;
	}

//	@GetMapping
//	public ResponseEntity<?> getUpdate(
//		@RequestParam long userId,
//		@RequestParam String token
//	) {
//		if (authService.isInvalidToken(userId, token)) {
//			return ResponseEntity.badRequest().build();
//		}
//
//		List<NewMessageUpdate> newMessageUpdates = updateService.getNewMessageUpdates(userId);
//		updateService.deleteAll(newMessageUpdates);
//
//		return ResponseEntity.ok(newMessageUpdates.stream().map(NewMessageUpdate::toPlain).toList());
//	}
}
