package ru.ares4322.distributedcounter.common;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

import static org.apache.commons.io.IOUtils.readLines;
import static org.slf4j.LoggerFactory.getLogger;

//TODO maybe create specific implementations and move them to specific modules
@Singleton
public class CliCommandReaderImpl implements CliCommandReader {

	private static final Logger log = getLogger(CliCommandReaderImpl.class);

	@Inject
	private Controllable controllable;

	@Override
	public void read() {
		try {
			for (String line : readLines(System.in)) {
				switch (line) {
					case "start":
						controllable.start();
						break;
					case "stop":
						controllable.stop();
						break;
					case "exit":
						controllable.exit();
						break;
					default:
						log.error("unknown command: {}", line);
				}
			}
		} catch (IOException e) {
			log.error("cli reading error", e);
		}
	}
}
