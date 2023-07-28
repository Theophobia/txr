package me.theophobia.txr.ws.event;

import lombok.*;
import me.theophobia.txr.IJson;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Event13 implements IJson {
	private long userId;
	private String token;
	private String receiver;
	private LocalDateTime timestamp;

	@Override
	public String json() {
		return "{\"userId=" + userId +
			", \"token\": \"" + token + "\"" +
			", \"receiver\": \"" + receiver + "\"" +
			", \"timestamp\": \"" + timestamp + "\"}";
	}
}
