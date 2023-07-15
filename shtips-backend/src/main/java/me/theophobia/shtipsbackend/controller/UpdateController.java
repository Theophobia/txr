package me.theophobia.shtipsbackend.controller;

import me.theophobia.shtipsbackend.Update;
import me.theophobia.shtipsbackend.service.AuthService;
import me.theophobia.shtipsbackend.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

	@GetMapping
	public ResponseEntity<?> getUpdate(
		@RequestParam long userId,
		@RequestParam String token
	) {
		if (authService.isInvalidToken(userId, token)) {
			return ResponseEntity.badRequest().build();
		}

		List<Update> updates = updateService.getUpdates(userId);
		updateService.deleteAll(updates);

		return ResponseEntity.ok(updates.stream().map(Update::toPlain).toList());
	}
}
