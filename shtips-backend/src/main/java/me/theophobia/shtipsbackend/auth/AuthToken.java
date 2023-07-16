package me.theophobia.shtipsbackend.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@EqualsAndHashCode
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
}


