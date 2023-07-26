package me.theophobia.shtipsbackend.user;

public record PasswordlessUser(long userId, String email, String username) {
}
