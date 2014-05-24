package ru.ares4322.distributedcounter.initiator.cfg;

import ru.ares4322.distributedcounter.common.cfg.Config;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.nio.file.Path;

@Singleton
public class InitiatorConfig extends Config {

	private final Path senderFilePath;
	private final Path receiverFilePath;

	@Inject
	public InitiatorConfig(
		@Named("senderThreads") Integer senderThreads,
		@Named("receiverThreads") Integer receiverThreads,
		@Named("localServerPort") Integer localServerPort,
		@Named("localServerAddress") String localServerAddress,
		@Named("remoteServerPort") Integer remoteServerPort,
		@Named("remoteServerAddress") String remoteServerAddress,
		@Named("senderFilePath") Path senderFilePath,
		@Named("receiverFilePath") Path receiverFilePath
	) {
		super(
			senderThreads,
			receiverThreads,
			localServerPort,
			localServerAddress,
			remoteServerPort,
			remoteServerAddress
		);
		this.senderFilePath = senderFilePath;
		this.receiverFilePath = receiverFilePath;
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
		if (senderFilePath != null ? !senderFilePath.equals(that.senderFilePath) : that.senderFilePath != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (senderFilePath != null ? senderFilePath.hashCode() : 0);
		result = 31 * result + (receiverFilePath != null ? receiverFilePath.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "InitiatorConfig{" +
			super.toString() +
			", senderFilePath=" + senderFilePath +
			", receiverFilePath=" + receiverFilePath +
			'}';
	}
}
