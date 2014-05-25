package ru.ares4322.distributedcounter.initiator.cfg;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.cfg.StartParamsParserException;
import ru.ares4322.distributedcounter.common.pool.ConnectionPoolConfig;
import ru.ares4322.distributedcounter.common.receiver.ReceiverConfig;
import ru.ares4322.distributedcounter.common.sender.SenderConfig;
import ru.ares4322.distributedcounter.common.sorter.WriterConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.math.NumberUtils.toInt;
import static org.slf4j.LoggerFactory.getLogger;
import static ru.ares4322.distributedcounter.initiator.cfg.CliParams.*;

//TODO refactor help printing
//TODO refactor params naming
//TODO add default logic for local server address and port
//TODO move common code from parsers
class InitiatorConfigParser {

	private static final Logger log = getLogger(InitiatorConfigParser.class);

	public Configs parse(String[] params) throws StartParamsParserException {
		int senderThreads = 0;
		int receiverThreads = 0;
		int localServerPort = 0;
		String localServerAddress = null;
		int remoteServerPort = 0;
		String remoteServerAddress = null;
		Path senderFilePath;
		Path receiverFilePath;

		OptionParser parser = new OptionParser(
			format(
				"%s::%s::%s::%s::%s:%s:%s:%s:",
				LOCAL_SERVER_PORT, LOCAL_SERVER_ADDRESS, REMOTE_SERVER_PORT, REMOTE_SERVER_ADDRESS, SENDER_FILE,
				RECEIVER_FILE, SENDER_THREADS, RECEIVER_THREADS
			)
		);
		parser.accepts(HELP);
		OptionSet optionSet = parser.parse(params);

		if (optionSet.has(HELP)) {
			throw new StartParamsParserException(help);
		}

		StringBuilder errorBuilder = new StringBuilder();
		boolean hasError = false;

		if (optionSet.has(LOCAL_SERVER_PORT) && optionSet.hasArgument(LOCAL_SERVER_PORT)) {
			int sp = toInt(valueOf(optionSet.valueOf(LOCAL_SERVER_PORT)), -1);
			if (sp == -1) {
				errorBuilder.append("server port must be number (1 - 65535); \n");
				hasError = true;
			}
			localServerPort = sp;
		} else {
			hasError = true;
			errorBuilder.append(format("enter server port: -%s server_port; \n", LOCAL_SERVER_PORT));
		}

		if (optionSet.has(LOCAL_SERVER_ADDRESS) && optionSet.hasArgument(LOCAL_SERVER_ADDRESS)) {
			localServerAddress = valueOf(optionSet.valueOf(LOCAL_SERVER_ADDRESS));
		} else {
			hasError = true;
			errorBuilder.append(format("enter server address: -%s server_address; \n", LOCAL_SERVER_ADDRESS));
		}

		if (optionSet.has(REMOTE_SERVER_PORT) && optionSet.hasArgument(REMOTE_SERVER_PORT)) {
			int sp = toInt(valueOf(optionSet.valueOf(REMOTE_SERVER_PORT)), -1);
			if (sp == -1) {
				errorBuilder.append("server port must be number (1 - 65535); \n");
				hasError = true;
			}
			remoteServerPort = sp;
		} else {
			hasError = true;
			errorBuilder.append(format("enter server port: -%s server_port; \n", LOCAL_SERVER_PORT));
		}

		if (optionSet.has(REMOTE_SERVER_ADDRESS) && optionSet.hasArgument(REMOTE_SERVER_ADDRESS)) {
			remoteServerAddress = valueOf(optionSet.valueOf(REMOTE_SERVER_ADDRESS));
		} else {
			hasError = true;
			errorBuilder.append(format("enter server address: -%s server_address; \n", LOCAL_SERVER_ADDRESS));
		}

		try {
			Path path;
			if (optionSet.has(RECEIVER_FILE) && optionSet.hasArgument(RECEIVER_FILE)) {
				path = Paths.get(valueOf(optionSet.valueOf(RECEIVER_FILE)));
				File file = path.toFile();
				//TODO need recreate file
				if (!file.exists()) {
					path = Files.createFile(path);
				}
			} else {
				path = Files.createTempFile(RECEIVE_LOGFILE_NAME, LOGFILE_NAME_SUFFIX);
				log.info("create temp receiver file: {}", path.toAbsolutePath());
			}
			receiverFilePath = path;
		} catch (IOException | InvalidPathException | SecurityException e) {
			throw new StartParamsParserException("temp receiver file creation error", e);
		}

		try {
			Path path;
			if (optionSet.has(SENDER_FILE) && optionSet.hasArgument(SENDER_FILE)) {
				path = Paths.get(valueOf(optionSet.valueOf(SENDER_FILE)));
				File file = path.toFile();
				//TODO need recreate file
				if (!file.exists()) {
					path = Files.createFile(path);
				}
			} else {
				path = Files.createTempFile(SEND_LOGFILE_NAME, LOGFILE_NAME_SUFFIX);
				log.info("create temp sender file: {}", path.toAbsolutePath());
			}
			senderFilePath = path;
		} catch (IOException | InvalidPathException | SecurityException e) {
			throw new StartParamsParserException("temp sender file creation error", e);
		}

		if (optionSet.has(SENDER_THREADS) && optionSet.hasArgument(SENDER_THREADS)) {
			int sp = toInt(valueOf(optionSet.valueOf(SENDER_THREADS)), -1);
			if (sp == -1 || sp < 1 || sp > 3) {
				errorBuilder.append("server threads must be number (1 - 3); \n");
				hasError = true;
			} else {
				senderThreads = sp;
			}
		} else {
			senderThreads = DEFAULT_SENDER_THREADS;
			log.info("set sender threads to default ({})", DEFAULT_SENDER_THREADS);
		}

		if (optionSet.has(RECEIVER_THREADS) && optionSet.hasArgument(RECEIVER_THREADS)) {
			int rt = toInt(valueOf(optionSet.valueOf(RECEIVER_THREADS)), -1);
			if (rt == -1 || rt < 1 || rt > 3) {
				errorBuilder.append("receiver threads must be number (1 - 3); \n");
				hasError = true;
			} else {
				receiverThreads = rt;
			}
		} else {
			receiverThreads = DEFAULT_RECEIVER_THREADS;
			log.info("set receiver threads to default ({})", DEFAULT_RECEIVER_THREADS);
		}

		if (hasError) {
			throw new StartParamsParserException(errorBuilder.toString());
		}

		return new Configs(
			new WriterConfig(senderFilePath),
			new WriterConfig(receiverFilePath),
			new ConnectionPoolConfig(remoteServerAddress, remoteServerPort, senderThreads),
			new ReceiverConfig(localServerAddress, localServerPort, receiverThreads),
			new SenderConfig(senderThreads)
		);
	}

	private static final String SEND_LOGFILE_NAME = "initiator_send";
	private static final String RECEIVE_LOGFILE_NAME = "initiator_receive";
	private static final String LOGFILE_NAME_SUFFIX = ".txt";
	private static final int DEFAULT_SENDER_THREADS = 3;
	private static final int DEFAULT_RECEIVER_THREADS = 3;

	//TODO add help string
	private static final String help = "";

	public static class Configs {
		private final WriterConfig senderWriterConfig;
		private final WriterConfig receiverWriterConfig;
		private final ConnectionPoolConfig connectionPoolConfig;
		private final ReceiverConfig receiverConfig;
		private final SenderConfig senderConfig;

		public Configs(
			WriterConfig senderWriterConfig,
			WriterConfig receiverWriterConfig,
			ConnectionPoolConfig connectionPoolConfig,
			ReceiverConfig receiverConfig,
			SenderConfig senderConfig
		) {
			this.senderWriterConfig = senderWriterConfig;
			this.receiverWriterConfig = receiverWriterConfig;
			this.connectionPoolConfig = connectionPoolConfig;
			this.receiverConfig = receiverConfig;
			this.senderConfig = senderConfig;
		}

		public WriterConfig getSenderWriterConfig() {
			return senderWriterConfig;
		}

		public WriterConfig getReceiverWriterConfig() {
			return receiverWriterConfig;
		}

		public ConnectionPoolConfig getConnectionPoolConfig() {
			return connectionPoolConfig;
		}

		public ReceiverConfig getReceiverConfig() {
			return receiverConfig;
		}

		public SenderConfig getSenderConfig() {
			return senderConfig;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Configs)) return false;

			Configs configs = (Configs) o;

			if (connectionPoolConfig != null ? !connectionPoolConfig.equals(configs.connectionPoolConfig) : configs.connectionPoolConfig != null)
				return false;
			if (receiverConfig != null ? !receiverConfig.equals(configs.receiverConfig) : configs.receiverConfig != null)
				return false;
			if (receiverWriterConfig != null ? !receiverWriterConfig.equals(configs.receiverWriterConfig) : configs.receiverWriterConfig != null)
				return false;
			if (senderConfig != null ? !senderConfig.equals(configs.senderConfig) : configs.senderConfig != null)
				return false;
			if (senderWriterConfig != null ? !senderWriterConfig.equals(configs.senderWriterConfig) : configs.senderWriterConfig != null)
				return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = senderWriterConfig != null ? senderWriterConfig.hashCode() : 0;
			result = 31 * result + (receiverWriterConfig != null ? receiverWriterConfig.hashCode() : 0);
			result = 31 * result + (connectionPoolConfig != null ? connectionPoolConfig.hashCode() : 0);
			result = 31 * result + (receiverConfig != null ? receiverConfig.hashCode() : 0);
			result = 31 * result + (senderConfig != null ? senderConfig.hashCode() : 0);
			return result;
		}

		@Override
		public String toString() {
			return "Configs{" +
				"senderWriterConfig=" + senderWriterConfig +
				", receiverWriterConfig=" + receiverWriterConfig +
				", connectionPoolConfig=" + connectionPoolConfig +
				", receiverConfig=" + receiverConfig +
				", senderConfig=" + senderConfig +
				'}';
		}
	}
}
