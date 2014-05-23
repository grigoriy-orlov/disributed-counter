package ru.ares4322.distributedcounter.common.cli;

import org.apache.commons.io.input.CloseShieldInputStream;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.System.in;
import static org.slf4j.LoggerFactory.getLogger;

//TODO maybe create specific implementations and move them to specific modules
public class CliCommandReaderImpl implements CliCommandReader {

	private static final Logger log = getLogger(CliCommandReaderImpl.class);

	private final Controllable controllable;

	private boolean isExit;

	@Inject
	public CliCommandReaderImpl(Controllable controllable) {
		this.controllable = controllable;
	}

	@Override
	public void readCommand() {
		//TODO move to init/destroy
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new CloseShieldInputStream(in)))) {
			String line = reader.readLine();
			switch (line) {
				case "start":
					controllable.start();
					break;
				case "stop":
					controllable.stop();
					break;
				case "exit":
					isExit = true;
					controllable.exit();
					return;
				default:
					log.error("unknown command: {}", line);
			}
		} catch (IOException e) {
			log.error("cli reading error", e);
		}
	}

	@Override
	public boolean isExit() {
		return isExit;
	}
}
