package me.theophobia.shtipsbackend.chat;

import java.time.LocalDateTime;

public record AnonymousMessage(
	String senderUsername,
	LocalDateTime timestamp,
	MessageDataType type,
	String data,
	String bonusData
) { }
