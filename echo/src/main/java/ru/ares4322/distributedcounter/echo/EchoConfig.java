package ru.ares4322.distributedcounter.echo;

import ru.ares4322.distributedcounter.common.cfg.Config;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.nio.file.Path;

@Singleton
public class EchoConfig extends Config {

	private final Path filePath;

	@Inject
	public EchoConfig(
		@Named("senderThreads") Integer senderThreads,
		@Named("receiverThreads") Integer receiverThreads,
		@Named("localServerPort") Integer localServerPort,
		@Named("localServerAddress") String localServerAddress,
		@Named("remoteServerPort") Integer remoteServerPort,
		@Named("remoteServerAddress") String remoteServerAddress,
		@Named("filePath") Path filePath
	) {
		super(
			senderThreads,
			receiverThreads,
			localServerPort,
			localServerAddress,
			remoteServerPort,
			remoteServerAddress
		);
		this.filePath = filePath;
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

		if (filePath != null ? !filePath.equals(that.filePath) : that.filePath != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "EchoStartParams{" +
			super.toString() +
			", filePath=" + filePath +
			'}';
	}
}
