package ru.ares4322.distributedcounter.common;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.slf4j.LoggerFactory.getLogger;

@Singleton
public class CliCommandReaderTask implements Runnable {

	private static final Logger log = getLogger(CliCommandReaderTask.class);

	@Inject
	private CliCommandReader reader;

	@Override
	public void run() {
		log.debug("start cli command reading");
		while (true) {
			reader.read();
		}
	}
}
