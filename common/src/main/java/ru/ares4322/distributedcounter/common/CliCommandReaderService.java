package ru.ares4322.distributedcounter.common;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.slf4j.LoggerFactory.getLogger;

@Singleton
public class CliCommandReaderService {

	private static final Logger log = getLogger(CliCommandReaderService.class);

	@Inject
	private CliCommandReader reader;

	public void run() {
		log.debug("startUp cli command reading");

		while (!reader.isExit()) {
			reader.readCommand();
		}
	}
}
