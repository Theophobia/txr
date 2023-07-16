package me.theophobia.shtipsbackend.util;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public final class Tuple3<A, B, C> {
	private A a = null;
	private B b = null;
	private C c = null;
}
