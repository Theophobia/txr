package me.theophobia.shtipsbackend.message;

public record SendMessageRequest(long userId, String token, String receiver, String message) {
}
