package me.theophobia.shtipsbackend.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor
@ToString
public final class AuthToken {
	@Id
	private final long userId;

	private final long timeCreated;

	private final long timeExpires;

	@Column(unique = true)
	private final String token;

	public AuthToken() {
		this(0, 0, 0, "");
	}

	public boolean isExpired() {
		return System.currentTimeMillis() > timeExpires;
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
}


