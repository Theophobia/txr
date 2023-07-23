package me.theophobia.shtipsbackend.ws.event;

import lombok.*;
import me.theophobia.shtipsbackend.activity.ActivityStatus;
import me.theophobia.shtipsbackend.IJson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Event31 implements IJson, Collector<Event31.Data, List<Event31.Data>, Event31> {

	private List<Data> data;

	@Override
	public Supplier<List<Data>> supplier() {
		return ArrayList::new;
	}

	@Override
	public BiConsumer<List<Data>, Data> accumulator() {
		return List::add;
	}

	@Override
	public BinaryOperator<List<Data>> combiner() {
		return (list1, list2) -> {
			list1.addAll(list2);
			return list1;
		};
	}

	@Override
	public Function<List<Data>, Event31> finisher() {
		return Event31::new;
	}

	@Override
	public Set<Characteristics> characteristics() {
		return Collections.emptySet();
	}

	public void addData(Data _data) {
		data.add(_data);
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class Data implements IJson{
		private String username;
		private ActivityStatus activity;

		@Override
		public String json() {
			return "{\"username\": \"" + username + "\", \"activity\": \"" + activity + "\"}";
		}
	}

	@Override
	public String json() {
		String mapped = '[' + String.join(", ", data.stream().map(Data::json).toList()) + ']';
		return "{\"data\": " + mapped + "}";
	}
}
