package ru.ares4322.distributedcounter.initiator;

import ru.ares4322.distributedcounter.common.Config;

import java.nio.file.Path;

//TODO add builder
public class InitiatorConfig extends Config {

	private final Integer remoteServerPort;
	private final String remoteServerAddress;
	private final Path senderFilePath;
	private final Path receiverFilePath;

	public InitiatorConfig(
		int senderThreads,
		int receiverThreads,
		int localServerPort,
		String localServerAddress,
		int remoteServerPort,
		String remoteServerAddress,
		Path senderFilePath,
		Path receiverFilePath
	) {
		super(senderThreads, receiverThreads, localServerPort, localServerAddress);
		this.remoteServerPort = remoteServerPort;
		this.remoteServerAddress = remoteServerAddress;
		this.senderFilePath = senderFilePath;
		this.receiverFilePath = receiverFilePath;
	}

	public Integer getRemoteServerPort() {
		return remoteServerPort;
	}

	public String getRemoteServerAddress() {
		return remoteServerAddress;
	}

	public Path getSenderFilePath() {
		return senderFilePath;
	}

	public Path getReceiverFilePath() {
		return receiverFilePath;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof InitiatorConfig)) return false;
		if (!super.equals(o)) return false;

		InitiatorConfig that = (InitiatorConfig) o;

		if (receiverFilePath != null ? !receiverFilePath.equals(that.receiverFilePath) : that.receiverFilePath != null)
			return false;
		if (remoteServerAddress != null ? !remoteServerAddress.equals(that.remoteServerAddress) : that.remoteServerAddress != null)
			return false;
		if (remoteServerPort != null ? !remoteServerPort.equals(that.remoteServerPort) : that.remoteServerPort != null)
			return false;
		if (senderFilePath != null ? !senderFilePath.equals(that.senderFilePath) : that.senderFilePath != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (remoteServerPort != null ? remoteServerPort.hashCode() : 0);
		result = 31 * result + (remoteServerAddress != null ? remoteServerAddress.hashCode() : 0);
		result = 31 * result + (senderFilePath != null ? senderFilePath.hashCode() : 0);
		result = 31 * result + (receiverFilePath != null ? receiverFilePath.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "InitiatorConfig{" +
			super.toString() +
			", remoteServerPort=" + remoteServerPort +
			", remoteServerAddress='" + remoteServerAddress + '\'' +
			", senderFilePath=" + senderFilePath +
			", receiverFilePath=" + receiverFilePath +
			'}';
	}
}
