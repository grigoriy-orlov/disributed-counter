package ru.ares4322.distributedcounter.initiator;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.Controllable;
import ru.ares4322.distributedcounter.common.CounterReceiverService;
import ru.ares4322.distributedcounter.common.CounterSenderService;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.LoggerFactory.getLogger;

//TODO add double command checking
public class ControllableImpl implements Controllable {

	private static final Logger log = getLogger(ControllableImpl.class);

	@Inject
	private CounterSenderService senderService;

	@Inject
	private CounterReceiverService receiverService;

	//TODO move to module
	private ExecutorService executor = newSingleThreadExecutor(
		new BasicThreadFactory.Builder().namingPattern("CounterSenderService-%s").build()
	);

	@Override
	public void init() {
		log.info("init counter sender");
		senderService.init();
		executor.execute(senderService);
	}

	@Override
	public void start() {
		log.info("startUp counter sender");
		senderService.startUp();
	}

	@Override
	public void stop() {
		log.info("stop counter sender");
		senderService.suspend();
	}

	@Override
	public void exit() {
		log.info("exit counter sender");
		senderService.shutDown();
		executor.shutdown();
		try {
			executor.awaitTermination(1, SECONDS);
		} catch (InterruptedException e) {
			log.error("termination waiting error", e);
		}
		//FIXME
		//receiverService.stopAsync().awaitTerminated();
	}
}
