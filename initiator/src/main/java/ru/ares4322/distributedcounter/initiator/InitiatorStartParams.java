package ru.ares4322.distributedcounter.initiator;

import ru.ares4322.distributedcounter.common.StartParams;

import java.nio.file.Path;

//TODO add builder
public class InitiatorStartParams extends StartParams {

	private final int remoteServerPort;
	private final String remoteServerAddress;
	private final Path senderFilePath;
	private final Path receiverFilePath;

	public InitiatorStartParams(
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

	public int getRemoteServerPort() {
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
		if (!(o instanceof InitiatorStartParams)) return false;
		if (!super.equals(o)) return false;

		InitiatorStartParams that = (InitiatorStartParams) o;

		if (remoteServerPort != that.remoteServerPort) return false;
		if (receiverFilePath != null ? !receiverFilePath.equals(that.receiverFilePath) : that.receiverFilePath != null)
			return false;
		if (remoteServerAddress != null ? !remoteServerAddress.equals(that.remoteServerAddress) : that.remoteServerAddress != null)
			return false;
		if (senderFilePath != null ? !senderFilePath.equals(that.senderFilePath) : that.senderFilePath != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + remoteServerPort;
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
