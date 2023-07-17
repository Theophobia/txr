package me.theophobia.shtipsbackend.ws.event;

import lombok.*;
import me.theophobia.shtipsbackend.IJson;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Event1 implements IJson {
	private long userId;
	private String token;

	@Override
	public String json() {
		return "{\"userId\": " + userId + "\"" +
			", \"token\": \"" + token + "\"}";
	}
}
