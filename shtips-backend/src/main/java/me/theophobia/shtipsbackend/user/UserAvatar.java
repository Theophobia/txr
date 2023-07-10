package me.theophobia.shtipsbackend.user;

import jakarta.persistence.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Entity
public class UserAvatar {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(columnDefinition = "bytea")
	private byte[] imageData;

	public UserAvatar() {
	}

	public UserAvatar(Long id, User user, byte[] imageData) {
		this.id = id;
		this.user = user;
		this.imageData = imageData;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public void setImage(BufferedImage image) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			byte[] imageBytes = baos.toByteArray();
			setImageData(imageBytes);
		}
		catch (Exception ignored) {

		}
	}
}
