package me.theophobia.shtipsbackend.ws.event;

import lombok.*;
import me.theophobia.shtipsbackend.IJson;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Event1 implements IJson {
	private long userId;
	private int slot;
	private String token;
	private List<String> channels;

	@Override
	public String json() {
		List<String> mapped = channels.stream().map(s -> "\"".concat(s).concat("\"")).toList();
		String joined = String.join(", ", mapped);
		return "{\"userId\": " + userId +
			", \"slot\": " + slot +
			", \"token\": \"" + token + "\"" +
			", \"channels\": [" + joined + "]}";
	}
}
