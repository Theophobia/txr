package me.theophobia.shtipsbackend;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.theophobia.shtipsbackend.util.Format;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDateTime;

@JsonComponent
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

	@Override
	public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		String formattedDateTime = localDateTime.format(Format.DATE_TIME_FORMATTER);
		jsonGenerator.writeString(formattedDateTime);
	}
}
