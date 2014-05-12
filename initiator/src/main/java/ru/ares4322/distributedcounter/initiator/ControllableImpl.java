package ru.ares4322.distributedcounter.initiator;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.Controllable;
import ru.ares4322.distributedcounter.common.CounterSenderService;

import javax.inject.Inject;

import static org.slf4j.LoggerFactory.getLogger;

//TODO add double command checking
public class ControllableImpl implements Controllable {

	private static final Logger log = getLogger(ControllableImpl.class);

	@Inject
	private CounterSenderService senderService;

	@Override
	public void start() {
		log.info("start counter sender");

		//TODO refactor
		switch (senderService.state()) {
			case NEW:
				senderService.startAsync().awaitRunning();
				break;
			case RUNNING:
				senderService.resume();
				break;
			default:
				log.error("sender service can't run, it's state is: {}", senderService.state());
		}

	}

	@Override
	public void stop() {
		log.info("stop counter sender");
		senderService.suspend();
	}

	@Override
	public void exit() {
		log.info("exit counter sender");
		senderService.stopAsync().awaitTerminated();
	}
}
