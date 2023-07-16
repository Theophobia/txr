package me.theophobia.shtipsbackend.chat;

import me.theophobia.shtipsbackend.IJson;

import java.time.LocalDateTime;

public record AnonymousMessage(
	String senderUsername,
	LocalDateTime timestamp,
	MessageDataType type,
	String data,
	String bonusData
) implements IJson {

	@Override
	public String toString() {
		return "{" +
			"senderUsername=" + senderUsername + ", " +
			"timestamp=" + timestamp + ", " +
			"type=" + type + ", " +
			"data=" + data + ", " +
			"bonusData=" + bonusData + '}';
	}

	@Override
	public String json() {
		return "{" +
			"\"senderUsername\": \"" + senderUsername + "\", " +
			"\"timestamp\": \"" + timestamp + "\", " +
			"\"type\": \"" + type + "\", " +
			"\"data\": \"" + data + "\", " +
			"\"bonusData\": \"" + bonusData + "\"}";
	}
}
