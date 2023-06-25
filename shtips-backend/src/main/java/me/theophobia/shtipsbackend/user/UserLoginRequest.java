package me.theophobia.shtipsbackend.user;

public record UserLoginRequest(String identifier,
							   UserLoginRequest.IdentifierType identifierType,
							   String password) {
	public enum IdentifierType {
		USERNAME, EMAIL
	}
}
