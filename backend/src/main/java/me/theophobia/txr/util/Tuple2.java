package me.theophobia.txr.util;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public final class Tuple2<A, B> {
	private A a = null;
	private B b = null;
}
