package ru.ares4322.distributedcounter.proxy;

import ru.ares4322.distributedcounter.common.Config;

//TODO add builder
public class ProxyConfig extends Config {

	private final int initiatorServerPort;
	private final String initiatorServerAddress;
	private final int echoServerPort;
	private final String echoServerAddress;
	private final int otherLocalServerPort;    //TODO add set of address-port pair
	private final String otherLocalServerAddress;

	public ProxyConfig(
		int senderThreads,
		int receiverThreads,
		int localServerPort,
		String localServerAddress,
		int initiatorServerPort,
		String initiatorServerAddress,
		int echoServerPort,
		String echoServerAddress,
		int otherLocalServerPort,
		String otherLocalServerAddress
	) {
		super(senderThreads, receiverThreads, localServerPort, localServerAddress);
		this.initiatorServerPort = initiatorServerPort;
		this.initiatorServerAddress = initiatorServerAddress;
		this.echoServerPort = echoServerPort;
		this.echoServerAddress = echoServerAddress;
		this.otherLocalServerPort = otherLocalServerPort;
		this.otherLocalServerAddress = otherLocalServerAddress;
	}

	public int getInitiatorServerPort() {
		return initiatorServerPort;
	}

	public String getInitiatorServerAddress() {
		return initiatorServerAddress;
	}

	public int getEchoServerPort() {
		return echoServerPort;
	}

	public String getEchoServerAddress() {
		return echoServerAddress;
	}

	public int getOtherLocalServerPort() {
		return otherLocalServerPort;
	}

	public String getOtherLocalServerAddress() {
		return otherLocalServerAddress;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProxyConfig)) return false;
		if (!super.equals(o)) return false;

		ProxyConfig that = (ProxyConfig) o;

		if (echoServerPort != that.echoServerPort) return false;
		if (initiatorServerPort != that.initiatorServerPort) return false;
		if (otherLocalServerPort != that.otherLocalServerPort) return false;
		if (echoServerAddress != null ? !echoServerAddress.equals(that.echoServerAddress) : that.echoServerAddress != null)
			return false;
		if (initiatorServerAddress != null ? !initiatorServerAddress.equals(that.initiatorServerAddress) : that.initiatorServerAddress != null)
			return false;
		if (otherLocalServerAddress != null ? !otherLocalServerAddress.equals(that.otherLocalServerAddress) : that.otherLocalServerAddress != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + initiatorServerPort;
		result = 31 * result + (initiatorServerAddress != null ? initiatorServerAddress.hashCode() : 0);
		result = 31 * result + echoServerPort;
		result = 31 * result + (echoServerAddress != null ? echoServerAddress.hashCode() : 0);
		result = 31 * result + otherLocalServerPort;
		result = 31 * result + (otherLocalServerAddress != null ? otherLocalServerAddress.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "ProxyStartParams{" +
			super.toString() +
			", initiatorServerPort=" + initiatorServerPort +
			", initiatorServerAddress='" + initiatorServerAddress + '\'' +
			", echoServerPort=" + echoServerPort +
			", echoServerAddress='" + echoServerAddress + '\'' +
			", otherLocalServerPort=" + otherLocalServerPort +
			", otherLocalServerAddress='" + otherLocalServerAddress + '\'' +
			'}';
	}
}
