package me.theophobia.shtipsbackend.util;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public final class Tuple4<A, B, C, D> {
	private A a = null;
	private B b = null;
	private C c = null;
	private D d = null;
}
