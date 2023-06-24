package me.theophobia.shtipsbackend;

public class UserLoginRequest {
	public enum IdentifierType {
		USERNAME, EMAIL
	}

	private final String identifier;
	private final IdentifierType identifierType;
	private final String password;

	public UserLoginRequest(String identifier, IdentifierType identifierType, String password) {
		this.identifier = identifier;
		this.identifierType = identifierType;
		this.password = password;
	}

	public String getIdentifier() {
		return identifier;
	}

	public IdentifierType getIdentifierType() {
		return identifierType;
	}

	public String getPassword() {
		return password;
	}
}
