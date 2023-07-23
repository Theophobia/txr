package me.theophobia.shtipsbackend.ws.event;

import lombok.*;
import me.theophobia.shtipsbackend.IJson;
import me.theophobia.shtipsbackend.chat.MessageDataType;
import me.theophobia.shtipsbackend.util.Format;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Event20 implements IJson {
	private String other_person_username;
	private LocalDateTime timestamp;
	private MessageDataType type;
	private String data;
	private String bonusData;

	@Override
	public String json() {
		return "{\"other_person_username\": \"" + other_person_username + "\"" +
			", \"timestamp\": \"" + Format.DATE_TIME_FORMATTER.format(timestamp) + "\"" +
			", \"type\": \"" + type + "\"" +
			", \"data\": \"" + data + "\"" +
			", \"bonusData\": " + (bonusData == null ? "null" : "\"" + bonusData + "\"") + "}";
	}
}
