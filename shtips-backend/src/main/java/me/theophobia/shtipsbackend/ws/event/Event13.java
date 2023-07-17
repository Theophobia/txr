package me.theophobia.shtipsbackend.ws.event;

import lombok.*;
import me.theophobia.shtipsbackend.IJson;

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
	private long pageSize;
	private long pageNumber;

	@Override
	public String json() {
		return "{\"userId=" + userId +
			", \"token\": \"" + token + "\"" +
			", \"receiver\": \"" + receiver + "\"" +
			", \"pageSize\": " + pageSize +
			", \"pageSize\": " + pageNumber + "\"}";
	}
}
