package me.theophobia.shtipsbackend.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public final class AuthToken {
	@Id
	private final long userId;
	private final long timeCreated;
	private final long timeExpires;
	@Column(unique = true)
	private final String token;

	public AuthToken(
		long userId,
		long timeCreated,
		long timeExpires,
		String token
	) {
		this.userId = userId;
		this.timeCreated = timeCreated;
		this.timeExpires = timeExpires;
		this.token = token;
	}

	public AuthToken() {
		this(0, 0, 0, "");
	}

	public long getUserId() {
		return userId;
	}

	public long getTimeCreated() {
		return timeCreated;
	}

	public long getTimeExpires() {
		return timeExpires;
	}

	public String getToken() {
		return token;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		var that = (AuthToken) obj;
		return this.userId == that.userId &&
			this.timeCreated == that.timeCreated &&
			this.timeExpires == that.timeExpires &&
			Objects.equals(this.token, that.token);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, timeCreated, timeExpires, token);
	}

	@Override
	public String toString() {
		return "AuthToken[" +
			"userId=" + userId + ", " +
			"timeCreated=" + timeCreated + ", " +
			"timeExpires=" + timeExpires + ", " +
			"token=" + token + ']';
	}
}


