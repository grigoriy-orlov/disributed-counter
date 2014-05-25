package ru.ares4322.distributedcounter.common.pool;

public class ConnectionPoolConfig {
	private final String serverAddress;
	private final int port;
	private final int size;

	public ConnectionPoolConfig(String serverAddress, int port, int size) {
		this.serverAddress = serverAddress;
		this.port = port;
		this.size = size;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public int getPort() {
		return port;
	}

	public int getSize() {
		return size;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ConnectionPoolConfig)) return false;

		ConnectionPoolConfig that = (ConnectionPoolConfig) o;

		if (port != that.port) return false;
		if (size != that.size) return false;
		if (serverAddress != null ? !serverAddress.equals(that.serverAddress) : that.serverAddress != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = serverAddress != null ? serverAddress.hashCode() : 0;
		result = 31 * result + port;
		result = 31 * result + size;
		return result;
	}

	@Override
	public String toString() {
		return "ConnectionPoolConfig{" +
			"serverAddress='" + serverAddress + '\'' +
			", port=" + port +
			", size=" + size +
			'}';
	}
}
