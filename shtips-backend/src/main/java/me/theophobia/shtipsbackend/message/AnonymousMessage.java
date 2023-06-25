package me.theophobia.shtipsbackend.message;

import java.time.LocalDateTime;

public record AnonymousMessage(
	String senderUsername,
	LocalDateTime timestamp,
	MessageDataType type,
	String data,
	String bonusData
) { }
