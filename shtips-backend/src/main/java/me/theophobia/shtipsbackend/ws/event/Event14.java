package me.theophobia.shtipsbackend.ws.event;

import lombok.*;
import me.theophobia.shtipsbackend.IJson;
import me.theophobia.shtipsbackend.chat.AnonymousSenderMessage;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Event14 implements IJson {
	private List<AnonymousSenderMessage> messages;

	@Override
	public String json() {
		String mapped = String.join(", ", messages.stream().map(AnonymousSenderMessage::json).toList());

		return "{\"messages\": [" + mapped + "]}";
	}
}
