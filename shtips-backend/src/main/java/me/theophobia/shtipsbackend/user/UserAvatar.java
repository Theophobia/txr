package me.theophobia.shtipsbackend.user;

import jakarta.persistence.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Entity
public final class UserAvatar {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(columnDefinition = "bytea")
	private byte[] imageData;

	@Column(columnDefinition = "bytea")
	private byte[] imageData36;

	public UserAvatar() {
	}

	public UserAvatar(Long id, User user, byte[] imageData, byte[] imageData36) {
		this.id = id;
		this.user = user;
		this.imageData = imageData;
		this.imageData36 = imageData36;
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

	public byte[] getImageData36() {
		return imageData36;
	}

	public void setImageData36(byte[] imageData36) {
		this.imageData36 = imageData36;
	}

	public void setImage(BufferedImage image) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			byte[] imageBytes = baos.toByteArray();

			setImageData(imageBytes);
		}
		catch (Exception ignored) {

		}

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(resize(image, 36, 36), "png", baos);
			byte[] imageBytes = baos.toByteArray();

			setImageData36(imageBytes);
		}
		catch (Exception ignored) {

		}
	}

	private static BufferedImage resize(BufferedImage originalImage, int newWidth, int newHeight) {
		// Create a new BufferedImage with the desired size
		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

		// Get the Graphics2D object to draw the resized image
		Graphics2D g2d = resizedImage.createGraphics();

		// Draw the original image onto the resized image
		g2d.drawImage(originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);

		// Dispose of the Graphics2D object
		g2d.dispose();

		return resizedImage;
	}
}
