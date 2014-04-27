package ru.ares4322.distributedcounter.echo;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.StartParamsParserException;

import javax.inject.Singleton;
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

//TODO refactor with other parsers
//TODO add test
@Singleton
public class EchoStartParamsParserImpl implements EchoStartParamsParser {

	private static final Logger log = getLogger(EchoStartParamsParserImpl.class);

	@Override
	public EchoStartParams parse(String[] params) throws StartParamsParserException {
		EchoStartParams result = new EchoStartParams();

		OptionParser parser = new OptionParser(
			format("%s::%s::%s:%s:%s:", SERVER_PORT, SERVER_ADDRESS, FILE, SENDER_THREADS, RECEIVER_THREADS)
		);
		parser.accepts(HELP);
		OptionSet optionSet = parser.parse(params);

		if (optionSet.has(HELP)) {
			throw new StartParamsParserException(help);
		}

		StringBuilder errorBuilder = new StringBuilder();
		boolean hasError = false;

		if (optionSet.has(SERVER_PORT) && optionSet.hasArgument(SERVER_PORT)) {
			int sp = toInt(valueOf(optionSet.valueOf(SERVER_PORT)), -1);
			if (sp == -1) {
				errorBuilder.append("server port must be number (1 - 65535); \n");
				hasError = true;
			}
			result.setServerPort(sp);
		} else {
			hasError = true;
			errorBuilder.append(format("enter server port: -%s server_port; \n", SERVER_PORT));
		}

		if (optionSet.has(SERVER_ADDRESS) && optionSet.hasArgument(SERVER_ADDRESS)) {
			result.setServerAddress(valueOf(optionSet.valueOf(SERVER_ADDRESS)));
		} else {
			hasError = true;
			errorBuilder.append(format("enter server address: -%s server_address; \n", SERVER_ADDRESS));
		}

		try {
			Path path;
			if (optionSet.has(FILE) && optionSet.hasArgument(FILE)) {
				path = Paths.get(valueOf(optionSet.valueOf(FILE)));
				File file = path.toFile();
				//TODO need recreate file?
				if (!file.exists()) {
					path = Files.createFile(path);
				}
			} else {
				path = Files.createTempFile(LOGFILE_NAME, LOGFILE_NAME_SUFFIX);
				log.info("create temp file: %s", path.toAbsolutePath());
			}
			result.setFilePath(path);
		} catch (IOException | InvalidPathException | SecurityException e) {
			throw new StartParamsParserException("temp file creation error:" + e);
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
	static final String FILE = "f";
	static final String SERVER_ADDRESS = "a";
	static final String SERVER_PORT = "p";
	private static final String HELP = "help";
	private static final String LOGFILE_NAME = "echo";
	private static final String LOGFILE_NAME_SUFFIX = ".txt";
	private static final int DEFAULT_SENDER_THREADS = 3;
	private static final int DEFAULT_RECEIVER_THREADS = 3;

	//TODO add help string
	private static final String help = "";
}
