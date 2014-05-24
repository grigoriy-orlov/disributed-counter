package ru.ares4322.distributedcounter.initiator.sender;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.sender.CounterSender;
import ru.ares4322.distributedcounter.common.sender.CounterSenderTask;

import javax.inject.Inject;

import static org.slf4j.LoggerFactory.getLogger;

class CounterSenderTaskImpl implements CounterSenderTask {

	private static final Logger log = getLogger(CounterSenderTaskImpl.class);

	private final CounterSender sender;

	private int counter;

	@Inject
	public CounterSenderTaskImpl(
		CounterSender sender
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
