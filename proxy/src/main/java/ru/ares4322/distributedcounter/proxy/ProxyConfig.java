package ru.ares4322.distributedcounter.proxy;

import ru.ares4322.distributedcounter.common.cfg.Config;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class ProxyConfig extends Config {

	private final Integer initiatorServerPort;
	private final String initiatorServerAddress;
	private final Integer echoServerPort;
	private final String echoServerAddress;
	private final Integer otherLocalServerPort;    //TODO add set of address-port pair
	private final String otherLocalServerAddress;

	@Inject
	public ProxyConfig(
		@Named("senderThreads") Integer senderThreads,
		@Named("receiverThreads") Integer receiverThreads,
		@Named("localServerPort") Integer localServerPort,
		@Named("localServerAddress") String localServerAddress,
		@Named("initiatorServerPort") Integer initiatorServerPort,
		@Named("initiatorServerAddress") String initiatorServerAddress,
		@Named("echoServerPort") Integer echoServerPort,
		@Named("echoServerAddress") String echoServerAddress,
		@Named("otherLocalServerPort") Integer otherLocalServerPort,
		@Named("otherLocalServerAddress") String otherLocalServerAddress
	) {
		super(senderThreads, receiverThreads, localServerPort, localServerAddress);
		this.initiatorServerPort = initiatorServerPort;
		this.initiatorServerAddress = initiatorServerAddress;
		this.echoServerPort = echoServerPort;
		this.echoServerAddress = echoServerAddress;
		this.otherLocalServerPort = otherLocalServerPort;
		this.otherLocalServerAddress = otherLocalServerAddress;
	}

	public Integer getInitiatorServerPort() {
		return initiatorServerPort;
	}

	public String getInitiatorServerAddress() {
		return initiatorServerAddress;
	}

	public Integer getEchoServerPort() {
		return echoServerPort;
	}

	public String getEchoServerAddress() {
		return echoServerAddress;
	}

	public Integer getOtherLocalServerPort() {
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

		if (echoServerAddress != null ? !echoServerAddress.equals(that.echoServerAddress) : that.echoServerAddress != null)
			return false;
		if (echoServerPort != null ? !echoServerPort.equals(that.echoServerPort) : that.echoServerPort != null)
			return false;
		if (initiatorServerAddress != null ? !initiatorServerAddress.equals(that.initiatorServerAddress) : that.initiatorServerAddress != null)
			return false;
		if (initiatorServerPort != null ? !initiatorServerPort.equals(that.initiatorServerPort) : that.initiatorServerPort != null)
			return false;
		if (otherLocalServerAddress != null ? !otherLocalServerAddress.equals(that.otherLocalServerAddress) : that.otherLocalServerAddress != null)
			return false;
		if (otherLocalServerPort != null ? !otherLocalServerPort.equals(that.otherLocalServerPort) : that.otherLocalServerPort != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (initiatorServerPort != null ? initiatorServerPort.hashCode() : 0);
		result = 31 * result + (initiatorServerAddress != null ? initiatorServerAddress.hashCode() : 0);
		result = 31 * result + (echoServerPort != null ? echoServerPort.hashCode() : 0);
		result = 31 * result + (echoServerAddress != null ? echoServerAddress.hashCode() : 0);
		result = 31 * result + (otherLocalServerPort != null ? otherLocalServerPort.hashCode() : 0);
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
