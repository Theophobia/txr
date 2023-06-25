package me.theophobia.shtipsbackend.user;

import jakarta.persistence.*;

@Entity
@Table(name = "users", uniqueConstraints = {
	@UniqueConstraint(columnNames = "username"),
	@UniqueConstraint(columnNames = "email")
})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String username;

	@Column(unique = true)
	private String email;

	private String password;

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
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
}
