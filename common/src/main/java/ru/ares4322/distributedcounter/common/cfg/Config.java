package ru.ares4322.distributedcounter.common.cfg;

public abstract class Config {
	private final Integer senderThreads;
	private final Integer receiverThreads;
	private final Integer localServerPort;
	private final String localServerAddress;

	protected Config(Integer senderThreads, Integer receiverThreads, Integer localServerPort, String localServerAddress) {
		this.senderThreads = senderThreads;
		this.receiverThreads = receiverThreads;
		this.localServerPort = localServerPort;
		this.localServerAddress = localServerAddress;
	}

	public Integer getSenderThreads() {
		return senderThreads;
	}

	public Integer getReceiverThreads() {
		return receiverThreads;
	}

	public Integer getLocalServerPort() {
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

		if (localServerAddress != null ? !localServerAddress.equals(config.localServerAddress) : config.localServerAddress != null)
			return false;
		if (localServerPort != null ? !localServerPort.equals(config.localServerPort) : config.localServerPort != null)
			return false;
		if (receiverThreads != null ? !receiverThreads.equals(config.receiverThreads) : config.receiverThreads != null)
			return false;
		if (senderThreads != null ? !senderThreads.equals(config.senderThreads) : config.senderThreads != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = senderThreads != null ? senderThreads.hashCode() : 0;
		result = 31 * result + (receiverThreads != null ? receiverThreads.hashCode() : 0);
		result = 31 * result + (localServerPort != null ? localServerPort.hashCode() : 0);
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
