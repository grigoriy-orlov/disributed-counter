package ru.ares4322.distributedcounter.common.domain;

public class Packet {

	public static final int size = 8;

	private final int state;
	private final int number;

	public Packet(int state, int number) {
		this.state = state;
		this.number = number;
	}

	public int getState() {
		return state;
	}

	public int getNumber() {
		return number;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Packet)) return false;

		Packet packet = (Packet) o;

		if (number != packet.number) return false;
		if (state != packet.state) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = state;
		result = 31 * result + number;
		return result;
	}

	@Override
	public String toString() {
		return "Packet{" +
			"state=" + state +
			", number=" + number +
			'}';
	}
}
