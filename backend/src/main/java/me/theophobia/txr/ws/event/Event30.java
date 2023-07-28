package me.theophobia.txr.ws.event;

import lombok.*;
import me.theophobia.txr.IJson;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Event30 implements IJson {
	private long userId;
	private List<String> usernames;

	@Override
	public String json() {
		String mapped = '[' + String.join(", ", usernames) + ']';
		return "{\"userId:\": " + userId + ", \"usernames\": " + mapped + "}";
	}
}
