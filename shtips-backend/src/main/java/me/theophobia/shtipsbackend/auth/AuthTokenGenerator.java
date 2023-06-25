package me.theophobia.shtipsbackend.auth;

import com.google.gson.Gson;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

public class AuthTokenGenerator {
	private static final long TOKEN_DURATION = Duration.of(3, ChronoUnit.HOURS).toMillis();

	private record Obj(long userId, long timeCreated, long timeExpires) {
		public AuthToken toToken(String hmac) {
			return new AuthToken(userId, timeCreated, timeExpires, hmac);
		}
	}

	public static AuthToken generate(long userId) {
		try {
			Gson gson = new Gson();

			long currTime = System.currentTimeMillis();
			Obj obj = new Obj(userId, currTime, currTime + TOKEN_DURATION);
			String message = gson.toJson(obj);
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
			String hmac = Base64.getEncoder().encodeToString(hmacBytes);

			return obj.toToken(hmac);
		}
		catch (final Exception e) {
			return null;
		}
	}
}
