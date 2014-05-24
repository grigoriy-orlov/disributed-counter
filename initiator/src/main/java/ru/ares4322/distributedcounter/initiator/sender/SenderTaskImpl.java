package ru.ares4322.distributedcounter.initiator.sender;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.sender.Sender;
import ru.ares4322.distributedcounter.common.sender.SenderTask;

import javax.inject.Inject;

import static org.slf4j.LoggerFactory.getLogger;

class SenderTaskImpl implements SenderTask {

	private static final Logger log = getLogger(SenderTaskImpl.class);

	private final Sender sender;

	private int counter;

	@Inject
	public SenderTaskImpl(
		Sender sender
	) {
		this.sender = sender;
	}

	@Override
	public void run() {
		log.debug("send data and write to file");
		sender.send(counter);
	}

	@Override
	public void setCounter(int counter) {
		this.counter = counter;
	}

}
