package ru.ares4322.distributedcounter.proxy;

public class ProxyStartParams {

	private int senderThreads;
	private int receiverThreads;
	private int initiatorServerPort;
	private String initiatorServerAddress;
	private int echoServerPort;
	private String echoServerAddress;

	public int getSenderThreads() {
		return senderThreads;
	}

	public void setSenderThreads(int senderThreads) {
		this.senderThreads = senderThreads;
	}

	public int getReceiverThreads() {
		return receiverThreads;
	}

	public void setReceiverThreads(int receiverThreads) {
		this.receiverThreads = receiverThreads;
	}

	public int getEchoServerPort() {
		return echoServerPort;
	}

	public void setEchoServerPort(int echoServerPort) {
		this.echoServerPort = echoServerPort;
	}

	public String getEchoServerAddress() {
		return echoServerAddress;
	}

	public void setEchoServerAddress(String echoServerAddress) {
		this.echoServerAddress = echoServerAddress;
	}

	public int getInitiatorServerPort() {
		return initiatorServerPort;
	}

	public void setInitiatorServerPort(int initiatorServerPort) {
		this.initiatorServerPort = initiatorServerPort;
	}

	public String getInitiatorServerAddress() {
		return initiatorServerAddress;
	}

	public void setInitiatorServerAddress(String initiatorServerAddress) {
		this.initiatorServerAddress = initiatorServerAddress;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProxyStartParams)) return false;

		ProxyStartParams that = (ProxyStartParams) o;

		if (echoServerPort != that.echoServerPort) return false;
		if (initiatorServerPort != that.initiatorServerPort) return false;
		if (receiverThreads != that.receiverThreads) return false;
		if (senderThreads != that.senderThreads) return false;
		if (echoServerAddress != null ? !echoServerAddress.equals(that.echoServerAddress) : that.echoServerAddress != null)
			return false;
		if (initiatorServerAddress != null ? !initiatorServerAddress.equals(that.initiatorServerAddress) : that.initiatorServerAddress != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = senderThreads;
		result = 31 * result + receiverThreads;
		result = 31 * result + initiatorServerPort;
		result = 31 * result + (initiatorServerAddress != null ? initiatorServerAddress.hashCode() : 0);
		result = 31 * result + echoServerPort;
		result = 31 * result + (echoServerAddress != null ? echoServerAddress.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "ProxyStartParams{" +
			"senderThreads=" + senderThreads +
			", receiverThreads=" + receiverThreads +
			", initiatorServerPort=" + initiatorServerPort +
			", initiatorServerAddress='" + initiatorServerAddress + '\'' +
			", echoServerPort=" + echoServerPort +
			", echoServerAddress='" + echoServerAddress + '\'' +
			'}';
	}
}
