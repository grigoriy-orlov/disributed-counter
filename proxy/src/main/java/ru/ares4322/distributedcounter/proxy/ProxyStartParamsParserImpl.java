package ru.ares4322.distributedcounter.proxy;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.StartParamsParserException;

import javax.inject.Singleton;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.math.NumberUtils.toInt;
import static org.slf4j.LoggerFactory.getLogger;

//TODO refactor with other parsers
//TODO add test
@Singleton
public class ProxyStartParamsParserImpl implements ProxyEchoStartParamsParser {

	private static final Logger log = getLogger(ProxyStartParamsParserImpl.class);

	@Override
	public ProxyStartParams parse(String[] params) throws StartParamsParserException {
		ProxyStartParams result = new ProxyStartParams();

		OptionParser parser = new OptionParser(
			format(
				"%s::%s::%s::%s::%s:%s:",
				INITIATOR_SERVER_PORT, INITIATOR_SERVER_ADDRESS, ECHO_SERVER_PORT,
				ECHO_SERVER_ADDRESS, SENDER_THREADS, RECEIVER_THREADS
			)
		);
		parser.accepts(HELP);
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
			result.setInitiatorServerPort(sp);
		} else {
			hasError = true;
			errorBuilder.append(format("enter initiator server port: -%s server_port; \n", INITIATOR_SERVER_PORT));
		}

		if (optionSet.has(INITIATOR_SERVER_ADDRESS) && optionSet.hasArgument(INITIATOR_SERVER_ADDRESS)) {
			result.setInitiatorServerAddress(valueOf(optionSet.valueOf(INITIATOR_SERVER_ADDRESS)));
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
			result.setEchoServerPort(sp);
		} else {
			hasError = true;
			errorBuilder.append(format("enter echo server port: -%s server_port; \n", ECHO_SERVER_PORT));
		}

		if (optionSet.has(ECHO_SERVER_ADDRESS) && optionSet.hasArgument(ECHO_SERVER_ADDRESS)) {
			result.setEchoServerAddress(valueOf(optionSet.valueOf(ECHO_SERVER_ADDRESS)));
		} else {
			hasError = true;
			errorBuilder.append(format("enter echo server address: -%s server_address; \n", ECHO_SERVER_ADDRESS));
		}

		if (optionSet.has(SENDER_THREADS) && optionSet.hasArgument(SENDER_THREADS)) {
			int st = toInt(valueOf(optionSet.valueOf(SENDER_THREADS)), -1);
			if (st == -1 || st < 1 || st > 3) {
				errorBuilder.append("server threads must be number (1 - 3); \n");
				hasError = true;
			} else {
				result.setSenderThreads(st);
			}
		} else {
			result.setReceiverThreads(DEFAULT_SENDER_THREADS);
			log.info("set sender threads to default ({})", DEFAULT_SENDER_THREADS);
		}

		if (optionSet.has(RECEIVER_THREADS) && optionSet.hasArgument(RECEIVER_THREADS)) {
			int rt = toInt(valueOf(optionSet.valueOf(RECEIVER_THREADS)), -1);
			if (rt == -1 || rt < 1 || rt > 3) {
				errorBuilder.append("receiver threads must be number (1 - 3); \n");
				hasError = true;
			} else {
				result.setReceiverThreads(rt);
			}
		} else {
			result.setReceiverThreads(DEFAULT_RECEIVER_THREADS);
			log.info("set receiver threads to default ({})", DEFAULT_RECEIVER_THREADS);
		}

		if (hasError) {
			throw new StartParamsParserException(errorBuilder.toString());
		}

		return result;
	}

	static final String SENDER_THREADS = "s";
	static final String RECEIVER_THREADS = "r";
	static final String ECHO_SERVER_ADDRESS = "a";
	static final String ECHO_SERVER_PORT = "b";
	static final String INITIATOR_SERVER_ADDRESS = "x";
	static final String INITIATOR_SERVER_PORT = "y";
	private static final String HELP = "help";
	private static final int DEFAULT_SENDER_THREADS = 3;
	private static final int DEFAULT_RECEIVER_THREADS = 3;

	//TODO add help string
	private static final String help = "";
}
