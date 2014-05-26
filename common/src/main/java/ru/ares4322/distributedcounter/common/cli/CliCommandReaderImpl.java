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
	private String lastCommand = "new";

	@Inject
	public CliCommandReaderImpl(Controllable controllable) {
		this.controllable = controllable;
	}

	@Override
	public void readCommand() {
		//TODO move to init/destroy
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new CloseShieldInputStream(in)))) {
			String command = reader.readLine();
			switch (lastCommand) {
				case "new":
					switch (command) {
						case "start":
							controllable.start();
							lastCommand = command;
							isExit = false;
							break;
						case "stop":
							isExit = false;
							break;
						case "exit":
							controllable.exit();
							lastCommand = command;
							isExit = true;
							return;
						default:
							log.error("unknown command: {}", command);
					}
					break;
				case "start":
					switch (command) {
						case "start":
							isExit = false;
							break;
						case "stop":
							controllable.stop();
							lastCommand = command;
							isExit = false;
							break;
						case "exit":
							controllable.exit();
							lastCommand = command;
							isExit = true;
							return;
						default:
							log.error("unknown command: {}", command);
					}
					break;
				case "stop":
					switch (command) {
						case "start":
							controllable.start();
							lastCommand = command;
							isExit = false;
							break;
						case "stop":
							isExit = false;
							break;
						case "exit":
							controllable.exit();
							lastCommand = command;
							isExit = true;
							return;
						default:
							log.error("unknown command: {}", command);
					}
					break;
				case "exit":
					lastCommand = command;
					isExit = true;
					return;
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
