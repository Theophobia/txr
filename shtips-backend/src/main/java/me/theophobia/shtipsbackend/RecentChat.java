package me.theophobia.shtipsbackend;

import me.theophobia.shtipsbackend.message.MessageDataType;

import java.time.LocalDateTime;

public record RecentChat(String other_person_username, LocalDateTime timestamp, String data, String bonus_data, MessageDataType type) {

}
