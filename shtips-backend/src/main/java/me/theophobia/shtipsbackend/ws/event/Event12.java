package me.theophobia.shtipsbackend.ws.event;

import lombok.*;
import me.theophobia.shtipsbackend.IJson;
import me.theophobia.shtipsbackend.util.Format;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Event12 implements IJson {
	private LocalDateTime timestamp;

	@Override
	public String json() {
		return "{\"timestamp\": \"" + Format.DATE_TIME_FORMATTER.format(timestamp) + "\"}";
	}
}
