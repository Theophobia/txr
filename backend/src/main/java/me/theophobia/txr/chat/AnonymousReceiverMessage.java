package me.theophobia.txr.chat;

import me.theophobia.txr.IJson;
import me.theophobia.txr.util.Format;

import java.time.LocalDateTime;

public record AnonymousReceiverMessage(
	Long messageId,
	String receiver,
	LocalDateTime timestamp,
	MessageDataType type,
	String data,
	String bonusData
) implements IJson {

	@Override
	public String toString() {
		return "{" +
			"messageId=" + messageId + ", " +
			"receiver=" + receiver + ", " +
			"timestamp=" + timestamp + ", " +
			"type=" + type + ", " +
			"data=" + data + ", " +
			"bonusData=" + bonusData + '}';
	}

	@Override
	public String json() {
		return "{" +
			"\"messageId\": " + messageId + ", " +
			"\"receiver\": \"" + receiver + "\", " +
			"\"timestamp\": \"" + Format.DATE_TIME_FORMATTER.format(timestamp) + "\", " +
			"\"type\": \"" + type + "\", " +
			"\"data\": \"" + data + "\", " +
			"\"bonusData\": " + (bonusData == null ? "null" : "\"" + bonusData + "\"") + "}";
	}
}
