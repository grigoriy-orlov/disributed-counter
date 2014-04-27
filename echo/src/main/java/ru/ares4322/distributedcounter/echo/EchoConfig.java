package ru.ares4322.distributedcounter.echo;

import ru.ares4322.distributedcounter.common.Config;

import java.nio.file.Path;

//TODO add builder
public class EchoConfig extends Config {

	private final int remoteServerPort;
	private final String remoteServerAddress;
	private final Path filePath;

	public EchoConfig(
		int senderThreads,
		int receiverThreads,
		int localServerPort,
		String localServerAddress,
		int remoteServerPort,
		String remoteServerAddress,
		Path filePath
	) {
		super(senderThreads, receiverThreads, localServerPort, localServerAddress);
		this.remoteServerPort = remoteServerPort;
		this.remoteServerAddress = remoteServerAddress;
		this.filePath = filePath;
	}

	public int getRemoteServerPort() {
		return remoteServerPort;
	}

	public String getRemoteServerAddress() {
		return remoteServerAddress;
	}

	public Path getFilePath() {
		return filePath;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof EchoConfig)) return false;
		if (!super.equals(o)) return false;

		EchoConfig that = (EchoConfig) o;

		if (remoteServerPort != that.remoteServerPort) return false;
		if (filePath != null ? !filePath.equals(that.filePath) : that.filePath != null) return false;
		if (remoteServerAddress != null ? !remoteServerAddress.equals(that.remoteServerAddress) : that.remoteServerAddress != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + remoteServerPort;
		result = 31 * result + (remoteServerAddress != null ? remoteServerAddress.hashCode() : 0);
		result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "EchoStartParams{" +
			super.toString() +
			", remoteServerPort=" + remoteServerPort +
			", remoteServerAddress='" + remoteServerAddress + '\'' +
			", filePath=" + filePath +
			'}';
	}
}
