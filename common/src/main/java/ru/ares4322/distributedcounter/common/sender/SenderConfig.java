package ru.ares4322.distributedcounter.common.sender;

public class SenderConfig {

	private final int threads;

	public SenderConfig(int threads) {
		this.threads = threads;
	}

	public int getThreads() {
		return threads;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SenderConfig)) return false;

		SenderConfig that = (SenderConfig) o;

		if (threads != that.threads) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return threads;
	}

	@Override
	public String toString() {
		return "SenderConfig{" +
			"threads=" + threads +
			'}';
	}
}
