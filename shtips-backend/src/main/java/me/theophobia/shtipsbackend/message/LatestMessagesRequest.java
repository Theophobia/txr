package me.theophobia.shtipsbackend.message;

public record LatestMessagesRequest(long userId, String token, String receiver, int pageNumber, int pageSize) {
}
