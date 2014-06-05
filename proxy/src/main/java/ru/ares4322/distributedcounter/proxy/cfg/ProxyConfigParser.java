package ru.ares4322.distributedcounter.proxy.cfg;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.cfg.StartParamsParserException;
import ru.ares4322.distributedcounter.common.pool.ConnectionPoolConfig;
import ru.ares4322.distributedcounter.common.receiver.ReceiverConfig;
import ru.ares4322.distributedcounter.common.sender.SenderConfig;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.math.NumberUtils.toInt;
import static org.slf4j.LoggerFactory.getLogger;
import static ru.ares4322.distributedcounter.proxy.cfg.CliParams.*;

//TODO refactor help printing
//TODO refactor params naming
//TODO add default logic for local server address and port
//TODO move common code from parsers
public class ProxyConfigParser {

	private static final Logger log = getLogger(ProxyConfigParser.class);

	public Configs parse(String[] params) throws StartParamsParserException {
		int senderThreads = 0;
		int receiverThreads = 0;
		int localServerPortForEcho = 0;
		String localServerAddressForEcho = null;
		int localServerPortForInitiator = 0;
		String localServerAddressForInitiator = null;
		int initiatorServerPort = 0;
		String initiatorServerAddress = null;
		int echoServerPort = 0;
		String echoServerAddress = null;

		OptionParser parser = new OptionParser();
		parser.accepts(INITIATOR_SERVER_ADDRESS).withRequiredArg().required();
		parser.accepts(INITIATOR_SERVER_PORT).withRequiredArg().required();
		parser.accepts(ECHO_SERVER_ADDRESS).withRequiredArg().required();
		parser.accepts(ECHO_SERVER_PORT).withRequiredArg().required();
		parser.accepts(LOCAL_SERVER_ADDRESS_FOR_ECHO).withRequiredArg().required();
		parser.accepts(LOCAL_SERVER_PORT_FOR_ECHO).withRequiredArg().required();
		parser.accepts(LOCAL_SERVER_ADDRESS_FOR_INITIATOR).withRequiredArg().required();
		parser.accepts(LOCAL_SERVER_PORT_FOR_INITIATOR).withRequiredArg().required();
		parser.accepts(ECHO_SENDER_THREADS).withRequiredArg();
		parser.accepts(ECHO_RECEIVER_THREADS).withRequiredArg();
		parser.accepts(INITIATOR_SENDER_THREADS).withRequiredArg();
		parser.accepts(INITIATOR_RECEIVER_THREADS).withRequiredArg();
		parser.accepts(HELP).isForHelp();
		OptionSet optionSet = parser.parse(params);

		if (optionSet.has(HELP)) {
			throw new StartParamsParserException(help);
		}

		StringBuilder errorBuilder = new StringBuilder();
		boolean hasError = false;

		if (optionSet.has(INITIATOR_SERVER_PORT) && optionSet.hasArgument(INITIATOR_SERVER_PORT)) {
			int sp = toInt(valueOf(optionSet.valueOf(INITIATOR_SERVER_PORT)), -1);
			if (sp == -1) {
				errorBuilder.append("server port must be number (1 - 65535); \n");
				hasError = true;
			}
			initiatorServerPort = sp;
		} else {
			hasError = true;
			errorBuilder.append(format("enter initiator server port: -%s server_port; \n", INITIATOR_SERVER_PORT));
		}

		if (optionSet.has(INITIATOR_SERVER_ADDRESS) && optionSet.hasArgument(INITIATOR_SERVER_ADDRESS)) {
			initiatorServerAddress = valueOf(optionSet.valueOf(INITIATOR_SERVER_ADDRESS));
		} else {
			hasError = true;
			errorBuilder.append(format("enter initiator server address: -%s server_address; \n", INITIATOR_SERVER_ADDRESS));
		}

		if (optionSet.has(ECHO_SERVER_PORT) && optionSet.hasArgument(ECHO_SERVER_PORT)) {
			int sp = toInt(valueOf(optionSet.valueOf(ECHO_SERVER_PORT)), -1);
			if (sp == -1) {
				errorBuilder.append("server port must be number (1 - 65535); \n");
				hasError = true;
			}
			echoServerPort = sp;
		} else {
			hasError = true;
			errorBuilder.append(format("enter echo server port: -%s server_port; \n", ECHO_SERVER_PORT));
		}

		if (optionSet.has(ECHO_SERVER_ADDRESS) && optionSet.hasArgument(ECHO_SERVER_ADDRESS)) {
			echoServerAddress = valueOf(optionSet.valueOf(ECHO_SERVER_ADDRESS));
		} else {
			hasError = true;
			errorBuilder.append(format("enter echo server address: -%s server_address; \n", ECHO_SERVER_ADDRESS));
		}

		if (optionSet.has(LOCAL_SERVER_PORT_FOR_ECHO) && optionSet.hasArgument(LOCAL_SERVER_PORT_FOR_ECHO)) {
			int sp = toInt(valueOf(optionSet.valueOf(LOCAL_SERVER_PORT_FOR_ECHO)), -1);
			if (sp == -1) {
				errorBuilder.append("server port must be number (1 - 65535); \n");
				hasError = true;
			}
			localServerPortForEcho = sp;
		} else {
			hasError = true;
			errorBuilder.append(format("enter server port: -%s server_port; \n", LOCAL_SERVER_PORT_FOR_ECHO));
		}

		if (optionSet.has(LOCAL_SERVER_ADDRESS_FOR_ECHO) && optionSet.hasArgument(LOCAL_SERVER_ADDRESS_FOR_ECHO)) {
			localServerAddressForEcho = valueOf(optionSet.valueOf(LOCAL_SERVER_ADDRESS_FOR_ECHO));
		} else {
			hasError = true;
			errorBuilder.append(format("enter server address: -%s server_address; \n", LOCAL_SERVER_ADDRESS_FOR_ECHO));
		}

		if (optionSet.has(LOCAL_SERVER_PORT_FOR_INITIATOR) && optionSet.hasArgument(LOCAL_SERVER_PORT_FOR_INITIATOR)) {
			int sp = toInt(valueOf(optionSet.valueOf(LOCAL_SERVER_PORT_FOR_INITIATOR)), -1);
			if (sp == -1) {
				errorBuilder.append("server port must be number (1 - 65535); \n");
				hasError = true;
			}
			localServerPortForInitiator = sp;
		} else {
			hasError = true;
			errorBuilder.append(format("enter server port: -%s server_port; \n", LOCAL_SERVER_PORT_FOR_ECHO));
		}

		if (optionSet.has(LOCAL_SERVER_ADDRESS_FOR_INITIATOR) && optionSet.hasArgument(LOCAL_SERVER_ADDRESS_FOR_INITIATOR)) {
			localServerAddressForInitiator = valueOf(optionSet.valueOf(LOCAL_SERVER_ADDRESS_FOR_INITIATOR));
		} else {
			hasError = true;
			errorBuilder.append(format("enter server address: -%s server_address; \n", LOCAL_SERVER_ADDRESS_FOR_ECHO));
		}

		if (optionSet.has(ECHO_SENDER_THREADS) && optionSet.hasArgument(ECHO_SENDER_THREADS)) {
			int st = toInt(valueOf(optionSet.valueOf(ECHO_SENDER_THREADS)), -1);
			if (st == -1 || st < 1 || st > 3) {
				errorBuilder.append("server threads must be number (1 - 3); \n");
				hasError = true;
			} else {
				senderThreads = st;
			}
		} else {
			senderThreads = DEFAULT_SENDER_THREADS;
			log.info("set sender threads to default ({})", DEFAULT_SENDER_THREADS);
		}

		if (optionSet.has(ECHO_RECEIVER_THREADS) && optionSet.hasArgument(ECHO_RECEIVER_THREADS)) {
			int rt = toInt(valueOf(optionSet.valueOf(ECHO_RECEIVER_THREADS)), -1);
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

		if (optionSet.has(INITIATOR_SENDER_THREADS) && optionSet.hasArgument(INITIATOR_SENDER_THREADS)) {
			int st = toInt(valueOf(optionSet.valueOf(INITIATOR_SENDER_THREADS)), -1);
			if (st == -1 || st < 1 || st > 3) {
				errorBuilder.append("server threads must be number (1 - 3); \n");
				hasError = true;
			} else {
				senderThreads = st;
			}
		} else {
			senderThreads = DEFAULT_SENDER_THREADS;
			log.info("set sender threads to default ({})", DEFAULT_SENDER_THREADS);
		}

		if (optionSet.has(INITIATOR_RECEIVER_THREADS) && optionSet.hasArgument(INITIATOR_RECEIVER_THREADS)) {
			int rt = toInt(valueOf(optionSet.valueOf(INITIATOR_RECEIVER_THREADS)), -1);
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
			new ConnectionPoolConfig(initiatorServerAddress, initiatorServerPort, senderThreads),
			new ConnectionPoolConfig(echoServerAddress, echoServerPort, senderThreads),
			new ReceiverConfig(localServerAddressForInitiator, localServerPortForInitiator, receiverThreads),
			new ReceiverConfig(localServerAddressForEcho, localServerPortForEcho, receiverThreads),
			new SenderConfig(senderThreads),
			new SenderConfig(senderThreads)
		);
	}

	private static final int DEFAULT_SENDER_THREADS = 3;
	private static final int DEFAULT_RECEIVER_THREADS = 3;

	//TODO add help string
	private static final String help = "";

	public static class Configs {
		private final ConnectionPoolConfig initiatorConnectionPoolConfig;
		private final ConnectionPoolConfig echoConnectionPoolConfig;
		private final ReceiverConfig initiatorReceiverConfig;
		private final ReceiverConfig echoReceiverConfig;
		private final SenderConfig initiatorSenderConfig;
		private final SenderConfig echoSenderConfig;

		public Configs(
			ConnectionPoolConfig initiatorConnectionPoolConfig,
			ConnectionPoolConfig echoConnectionPoolConfig,
			ReceiverConfig initiatorReceiverConfig,
			ReceiverConfig echoReceiverConfig,
			SenderConfig initiatorSenderConfig,
			SenderConfig echoSenderConfig
		) {
			this.initiatorConnectionPoolConfig = initiatorConnectionPoolConfig;
			this.echoConnectionPoolConfig = echoConnectionPoolConfig;
			this.initiatorReceiverConfig = initiatorReceiverConfig;
			this.echoReceiverConfig = echoReceiverConfig;
			this.initiatorSenderConfig = initiatorSenderConfig;
			this.echoSenderConfig = echoSenderConfig;
		}

		public ConnectionPoolConfig getInitiatorConnectionPoolConfig() {
			return initiatorConnectionPoolConfig;
		}

		public ConnectionPoolConfig getEchoConnectionPoolConfig() {
			return echoConnectionPoolConfig;
		}

		public ReceiverConfig getInitiatorReceiverConfig() {
			return initiatorReceiverConfig;
		}

		public ReceiverConfig getEchoReceiverConfig() {
			return echoReceiverConfig;
		}

		public SenderConfig getInitiatorSenderConfig() {
			return initiatorSenderConfig;
		}

		public SenderConfig getEchoSenderConfig() {
			return echoSenderConfig;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Configs)) return false;

			Configs configs = (Configs) o;

			if (echoConnectionPoolConfig != null ? !echoConnectionPoolConfig.equals(configs.echoConnectionPoolConfig) : configs.echoConnectionPoolConfig != null)
				return false;
			if (echoReceiverConfig != null ? !echoReceiverConfig.equals(configs.echoReceiverConfig) : configs.echoReceiverConfig != null)
				return false;
			if (echoSenderConfig != null ? !echoSenderConfig.equals(configs.echoSenderConfig) : configs.echoSenderConfig != null)
				return false;
			if (initiatorConnectionPoolConfig != null ? !initiatorConnectionPoolConfig.equals(configs.initiatorConnectionPoolConfig) : configs.initiatorConnectionPoolConfig != null)
				return false;
			if (initiatorReceiverConfig != null ? !initiatorReceiverConfig.equals(configs.initiatorReceiverConfig) : configs.initiatorReceiverConfig != null)
				return false;
			if (initiatorSenderConfig != null ? !initiatorSenderConfig.equals(configs.initiatorSenderConfig) : configs.initiatorSenderConfig != null)
				return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = initiatorConnectionPoolConfig != null ? initiatorConnectionPoolConfig.hashCode() : 0;
			result = 31 * result + (echoConnectionPoolConfig != null ? echoConnectionPoolConfig.hashCode() : 0);
			result = 31 * result + (initiatorReceiverConfig != null ? initiatorReceiverConfig.hashCode() : 0);
			result = 31 * result + (echoReceiverConfig != null ? echoReceiverConfig.hashCode() : 0);
			result = 31 * result + (initiatorSenderConfig != null ? initiatorSenderConfig.hashCode() : 0);
			result = 31 * result + (echoSenderConfig != null ? echoSenderConfig.hashCode() : 0);
			return result;
		}

		@Override
		public String toString() {
			return "Configs{" +
				"initiatorConnectionPoolConfig=" + initiatorConnectionPoolConfig +
				", echoConnectionPoolConfig=" + echoConnectionPoolConfig +
				", initiatorReceiverConfig=" + initiatorReceiverConfig +
				", echoReceiverConfig=" + echoReceiverConfig +
				", initiatorSenderConfig=" + initiatorSenderConfig +
				", echoSenderConfig=" + echoSenderConfig +
				'}';
		}
	}
}
