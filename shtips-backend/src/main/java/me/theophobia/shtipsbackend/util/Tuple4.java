package me.theophobia.shtipsbackend.util;

import java.util.Objects;

public final class Tuple4<A, B, C, D> {
	private A a = null;
	private B b = null;
	private C c = null;
	private D d = null;

	public Tuple4() {

	}

	public Tuple4(A a, B b, C c, D d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
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

	public D d() {
		return d;
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

	public void setD(D d) {
		this.d = d;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		var that = (Tuple4) obj;
		return Objects.equals(this.a, that.a) &&
			Objects.equals(this.b, that.b) &&
			Objects.equals(this.c, that.c) &&
			Objects.equals(this.d, that.d);
	}

	@Override
	public int hashCode() {
		return Objects.hash(a, b, c, d);
	}

	@Override
	public String toString() {
		return "Tuple4[" +
			"a=" + a + ", " +
			"b=" + b + ", " +
			"c=" + c + ", " +
			"d=" + d + ']';
	}

}
