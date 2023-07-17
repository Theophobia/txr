package me.theophobia.shtipsbackend.chat;

import me.theophobia.shtipsbackend.IJson;
import me.theophobia.shtipsbackend.util.Format;

import java.time.LocalDateTime;

public record AnonymousReceiverMessage(
	String receiver,
	LocalDateTime timestamp,
	MessageDataType type,
	String data,
	String bonusData
) implements IJson {

	@Override
	public String toString() {
		return "{" +
			"receiver=" + receiver + ", " +
			"timestamp=" + timestamp + ", " +
			"type=" + type + ", " +
			"data=" + data + ", " +
			"bonusData=" + bonusData + '}';
	}

	@Override
	public String json() {
		return "{" +
			"\"receiver\": \"" + receiver + "\", " +
			"\"timestamp\": \"" + Format.DATE_TIME_FORMATTER.format(timestamp) + "\", " +
			"\"type\": \"" + type + "\", " +
			"\"data\": \"" + data + "\", " +
			"\"bonusData\": " + (bonusData == null ? "null" : "\"" + bonusData + "\"") + "}";
	}
}
