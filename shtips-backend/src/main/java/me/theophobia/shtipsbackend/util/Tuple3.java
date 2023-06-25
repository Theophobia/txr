package me.theophobia.shtipsbackend.util;

import java.util.Objects;

public final class Tuple3<A, B, C> {
	private A a = null;
	private B b = null;
	private C c = null;

	public Tuple3() {

	}

	public Tuple3(A a, B b, C c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public A a() {
		return a;
	}

	public B b() {
		return b;
	}

	public C c() {
		return c;
	}

	public void setA(A a) {
		this.a = a;
	}

	public void setB(B b) {
		this.b = b;
	}

	public void setC(C c) {
		this.c = c;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		var that = (Tuple3) obj;
		return Objects.equals(this.a, that.a) &&
			Objects.equals(this.b, that.b) &&
			Objects.equals(this.c, that.c);
	}

	@Override
	public int hashCode() {
		return Objects.hash(a, b, c);
	}

	@Override
	public String toString() {
		return "Tuple3[" +
			"a=" + a + ", " +
			"b=" + b + ", " +
			"c=" + c + ']';
	}

}
