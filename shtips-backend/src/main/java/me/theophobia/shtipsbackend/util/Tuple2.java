package me.theophobia.shtipsbackend.util;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public final class Tuple2<A, B> {
	private A a = null;
	private B b = null;
}
