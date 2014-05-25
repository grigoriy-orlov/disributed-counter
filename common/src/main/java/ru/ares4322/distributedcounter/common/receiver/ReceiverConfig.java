package ru.ares4322.distributedcounter.common.receiver;

public class ReceiverConfig {
	private final String serverAddress;
	private final int port;
	private final int threads;

	public ReceiverConfig(String serverAddress, int port, int threads) {
		this.serverAddress = serverAddress;
		this.port = port;
		this.threads = threads;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public int getPort() {
		return port;
	}

	public int getThreads() {
		return threads;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ReceiverConfig)) return false;

		ReceiverConfig that = (ReceiverConfig) o;

		if (port != that.port) return false;
		if (threads != that.threads) return false;
		if (serverAddress != null ? !serverAddress.equals(that.serverAddress) : that.serverAddress != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = serverAddress != null ? serverAddress.hashCode() : 0;
		result = 31 * result + port;
		result = 31 * result + threads;
		return result;
	}

	@Override
	public String toString() {
		return "ReceiverConfig{" +
			"serverAddress='" + serverAddress + '\'' +
			", port=" + port +
			", threads=" + threads +
			'}';
	}
}
