package me.theophobia.shtipsbackend.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.theophobia.shtipsbackend.util.Format;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

	@Override
	public void write(JsonWriter out, LocalDateTime time) throws IOException {
		if (time == null) {
			out.nullValue();
		}
		else {
			out.value(time.format(Format.DATE_TIME_FORMATTER));
		}
	}

	@Override
	public LocalDateTime read(JsonReader in) throws IOException {
		LocalDateTime time = LocalDateTime.parse(in.nextString(), Format.DATE_TIME_FORMATTER);
		return time;
	}
}
