package me.theophobia.shtipsbackend.chat;

import java.time.LocalDateTime;

public record RecentChat(String other_person_username, LocalDateTime timestamp, String data, String bonus_data, MessageDataType type) {

}
