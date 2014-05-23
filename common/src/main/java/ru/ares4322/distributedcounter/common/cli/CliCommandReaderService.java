package ru.ares4322.distributedcounter.common.cli;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.slf4j.LoggerFactory.getLogger;

@Singleton
public class CliCommandReaderService {

	private static final Logger log = getLogger(CliCommandReaderService.class);

	private final CliCommandReader reader;

	@Inject
	public CliCommandReaderService(CliCommandReader reader) {
		this.reader = reader;
	}

	public void run() {
		log.debug("startUp cli command reading");

		while (!reader.isExit()) {
			reader.readCommand();
		}
	}
}
