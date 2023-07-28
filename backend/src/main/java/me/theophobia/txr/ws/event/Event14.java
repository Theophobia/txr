package me.theophobia.txr.ws.event;

import lombok.*;
import me.theophobia.txr.IJson;
import me.theophobia.txr.chat.AnonymousSenderMessage;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Event14 implements IJson {
	private String sender;
	private List<AnonymousSenderMessage> messages;

	@Override
	public String json() {
		String mapped = String.join(", ", messages.stream().map(AnonymousSenderMessage::json).toList());

		return "{\"sender\": \"" + sender + "\", \"messages\": [" + mapped + "]}";
	}
}
