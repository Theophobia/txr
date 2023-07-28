package me.theophobia.txr.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "users", uniqueConstraints = {
	@UniqueConstraint(columnNames = "username"),
	@UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public final class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String username;

	@Column(unique = true)
	private String email;

	private String password;

	public User(String email, String username, String password) {
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public boolean hasStrongPassword() {
		int lowercaseCount = 0;
		int uppercaseCount = 0;
		int digitCount = 0;
		int symbolCount = 0;

		for (char c : password.toCharArray()) {
			if (Character.isLowerCase(c)) {
				lowercaseCount++;
			}
			else if (Character.isUpperCase(c)) {
				uppercaseCount++;
			}
			else if (Character.isDigit(c)) {
				digitCount++;
			}
			else if (isSymbol(c)) {
				symbolCount++;
			}
		}

		return lowercaseCount >= 1 && uppercaseCount >= 1 && digitCount >= 1 && symbolCount >= 1;
	}

	public boolean passwordMatches(String password) {
		return this.password.equals(password);
	}

	public PasswordlessUser toPasswordlessUser() {
		return new PasswordlessUser(id, email, username);
	}

	private static boolean isSymbol(char c) {
		return c == '!' ||
			c == '@' ||
			c == '#' ||
			c == '$' ||
			c == '%' ||
			c == '^' ||
			c == '&' ||
			c == '*' ||
			c == '(' ||
			c == ')' ||
			c == '-' ||
			c == '_' ||
			c == '=' ||
			c == '+';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		User user = (User) o;
		return getEmail().equals(user.getEmail());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getEmail());
	}
}
