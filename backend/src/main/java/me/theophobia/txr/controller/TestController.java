package me.theophobia.txr.controller;

import me.theophobia.txr.service.AuthService;
import me.theophobia.txr.service.MessageService;
import me.theophobia.txr.service.UserAvatarService;
import me.theophobia.txr.service.UserService;
import me.theophobia.txr.user.User;
import me.theophobia.txr.user.UserAvatar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/test")
public final class TestController {

	private final MessageService messageService;
	private final AuthService authService;
	private final UserService userService;
	private final UserAvatarService userAvatarService;

	@Autowired
	public TestController(MessageService messageService, AuthService authService, UserService userService, UserAvatarService userAvatarService) {
		this.messageService = messageService;
		this.authService = authService;
		this.userService = userService;
		this.userAvatarService = userAvatarService;
	}

	@PostMapping(path = "/addAvatar")
	public ResponseEntity<?> addAvatar(
		@RequestParam("file") MultipartFile file,
		@RequestParam long userId
	) {
		Optional<User> optUser = userService.getUser(userId);
		if (optUser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not find specified user");
		}
		User user = optUser.get();

		BufferedImage image;
		try {
			image = ImageIO.read(file.getInputStream());
		}
		catch (Exception e) {
			return ResponseEntity.internalServerError().body("Failed to read image");
		}

		UserAvatar userAvatar = new UserAvatar();
		userAvatar.setImage(image);
//		try {
//			userAvatar.setImageData(file.getBytes());
//		}
//		catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read image");
//		}
		userAvatar.setUser(user);

		userAvatarService.save(userAvatar);

		return ResponseEntity.ok().build();
	}

}
