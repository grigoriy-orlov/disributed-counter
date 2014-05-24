package ru.ares4322.distributedcounter.proxy;

import ru.ares4322.distributedcounter.common.cfg.Config;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class ProxyConfig extends Config {

	private final Integer otherServerPort;
	private final String otherServerAddress;
	private final Integer otherLocalServerPort;    //TODO add set of address-port pair
	private final String otherLocalServerAddress;

	@Inject
	public ProxyConfig(
		@Named("senderThreads") Integer senderThreads,
		@Named("receiverThreads") Integer receiverThreads,
		@Named("localServerPort") Integer localServerPort,
		@Named("localServerAddress") String localServerAddress,
		@Named("initiatorServerPort") Integer remoteServerPort,
		@Named("initiatorServerAddress") String remoteServerAddress,
		@Named("echoServerPort") Integer otherServerPort,
		@Named("echoServerAddress") String otherServerAddress,
		@Named("otherLocalServerPort") Integer otherLocalServerPort,
		@Named("otherLocalServerAddress") String otherLocalServerAddress
	) {
		super(
			senderThreads,
			receiverThreads,
			localServerPort,
			localServerAddress,
			remoteServerPort,
			remoteServerAddress
		);
		this.otherServerPort = otherServerPort;
		this.otherServerAddress = otherServerAddress;
		this.otherLocalServerPort = otherLocalServerPort;
		this.otherLocalServerAddress = otherLocalServerAddress;
	}

	public Integer getOtherServerPort() {
		return otherServerPort;
	}

	public String getOtherServerAddress() {
		return otherServerAddress;
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

		if (otherServerAddress != null ? !otherServerAddress.equals(that.otherServerAddress) : that.otherServerAddress != null)
			return false;
		if (otherServerPort != null ? !otherServerPort.equals(that.otherServerPort) : that.otherServerPort != null)
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
		result = 31 * result + (otherServerPort != null ? otherServerPort.hashCode() : 0);
		result = 31 * result + (otherServerAddress != null ? otherServerAddress.hashCode() : 0);
		result = 31 * result + (otherLocalServerPort != null ? otherLocalServerPort.hashCode() : 0);
		result = 31 * result + (otherLocalServerAddress != null ? otherLocalServerAddress.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "ProxyStartParams{" +
			super.toString() +
			", echoServerPort=" + otherServerPort +
			", echoServerAddress='" + otherServerAddress + '\'' +
			", otherLocalServerPort=" + otherLocalServerPort +
			", otherLocalServerAddress='" + otherLocalServerAddress + '\'' +
			'}';
	}
}
