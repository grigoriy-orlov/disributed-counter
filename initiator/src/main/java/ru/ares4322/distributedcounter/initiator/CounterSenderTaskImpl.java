package ru.ares4322.distributedcounter.initiator;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.CounterSender;
import ru.ares4322.distributedcounter.common.CounterSenderTask;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.valueOf;
import static org.slf4j.LoggerFactory.getLogger;

public class CounterSenderTaskImpl implements CounterSenderTask {

	private static final Logger log = getLogger(CounterSenderTaskImpl.class);

	private int counter;

	@Inject
	private CounterSender sender;
	private BufferedWriter writer;

	@Override
	public void run() {
		checkNotNull(writer, "writer must be not null");

		log.debug("send data and write to file");
		sender.send(counter);
		try {
			writer.write(valueOf(counter) + "\n");
			writer.flush();
		} catch (IOException e) {
			log.error("writing to file error", e);
		}
	}

	@Override
	public void setCounter(int counter) {
		this.counter = counter;
	}

	@Override
	public void setWriter(BufferedWriter writer) {
		this.writer = writer;
	}
}
