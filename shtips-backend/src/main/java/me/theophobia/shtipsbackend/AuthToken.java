package me.theophobia.shtipsbackend;

import com.google.gson.Gson;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

public class AuthToken {
	private static final long TOKEN_DURATION = Duration.of(3, ChronoUnit.HOURS).toMillis();

	private final Long userId;
	private final long timeCreated;
	private final long timeExpires;

	public AuthToken(Long userId) {
		this.userId = userId;
		timeCreated = System.currentTimeMillis();
		timeExpires = timeCreated + TOKEN_DURATION;
	}

	public Long getUserId() {
		return userId;
	}

	public long getTimeCreated() {
		return timeCreated;
	}

	public long getTimeExpires() {
		return timeExpires;
	}

	public String generate() {
		try {
			Gson gson = new Gson();
			String message = gson.toJson(this);
			String secretKey = "somesecretkey";

			// Create a Mac object with the desired HMAC algorithm
			Mac mac = Mac.getInstance("HmacSHA256");

			// Create a SecretKeySpec object with the secret key
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

			// Initialize the Mac object with the secret key
			mac.init(secretKeySpec);

			// Compute the HMAC value for the message
			byte[] hmacBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

			// Convert the HMAC bytes to a Base64-encoded string
			return Base64.getEncoder().encodeToString(hmacBytes);
		}
		catch (final Exception e) {
			return "";
		}
	}
}
