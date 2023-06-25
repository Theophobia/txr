package me.theophobia.shtipsbackend.util;

import java.util.Objects;

public final class Tuple2<A, B> {
	private A a = null;
	private B b = null;

	public Tuple2() {
	}

	public Tuple2(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A a() {
		return a;
	}

	public B b() {
		return b;
	}

	public void a(A a) {
		this.a = a;
	}

	public void b(B b) {
		this.b = b;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		var that = (Tuple2) obj;
		return Objects.equals(this.a, that.a) &&
			Objects.equals(this.b, that.b);
	}

	@Override
	public int hashCode() {
		return Objects.hash(a, b);
	}

	@Override
	public String toString() {
		return "Tuple2[" +
			"a=" + a + ", " +
			"b=" + b + ']';
	}
}
