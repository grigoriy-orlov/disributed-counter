package ru.ares4322.distributedcounter.common;

public abstract class StartParams {
	private final int senderThreads;
	private final int receiverThreads;
	private final int localServerPort;
	private final String localServerAddress;

	protected StartParams(int senderThreads, int receiverThreads, int localServerPort, String localServerAddress) {
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
		if (!(o instanceof StartParams)) return false;

		StartParams startParams = (StartParams) o;

		if (receiverThreads != startParams.receiverThreads) return false;
		if (senderThreads != startParams.senderThreads) return false;
		if (localServerPort != startParams.localServerPort) return false;
		if (localServerAddress != null ? !localServerAddress.equals(startParams.localServerAddress) : startParams.localServerAddress != null)
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
