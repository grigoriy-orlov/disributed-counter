package ru.ares4322.distributedcounter.common;

public abstract class Config {
	private final int senderThreads;
	private final int receiverThreads;
	private final int localServerPort;
	private final String localServerAddress;

	protected Config(int senderThreads, int receiverThreads, int localServerPort, String localServerAddress) {
		this.senderThreads = senderThreads;
		this.receiverThreads = receiverThreads;
		this.localServerPort = localServerPort;
		this.localServerAddress = localServerAddress;
	}

	public int getSenderThreads() {
		return senderThreads;
	}

	public int getReceiverThreads() {
		return receiverThreads;
	}

	public int getLocalServerPort() {
		return localServerPort;
	}

	public String getLocalServerAddress() {
		return localServerAddress;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Config)) return false;

		Config config = (Config) o;

		if (receiverThreads != config.receiverThreads) return false;
		if (senderThreads != config.senderThreads) return false;
		if (localServerPort != config.localServerPort) return false;
		if (localServerAddress != null ? !localServerAddress.equals(config.localServerAddress) : config.localServerAddress != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = senderThreads;
		result = 31 * result + receiverThreads;
		result = 31 * result + localServerPort;
		result = 31 * result + (localServerAddress != null ? localServerAddress.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Config{" +
			"senderThreads=" + senderThreads +
			", receiverThreads=" + receiverThreads +
			", localServerPort=" + localServerPort +
			", localServerAddress='" + localServerAddress + '\'' +
			'}';
	}
}
