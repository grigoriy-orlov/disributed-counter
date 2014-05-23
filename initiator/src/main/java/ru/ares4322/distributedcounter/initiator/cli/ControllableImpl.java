package ru.ares4322.distributedcounter.initiator.cli;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.cli.Controllable;
import ru.ares4322.distributedcounter.common.receiver.CounterReceiverService;
import ru.ares4322.distributedcounter.common.sender.CounterSenderService;
import ru.ares4322.distributedcounter.common.sorter.SorterService;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.slf4j.LoggerFactory.getLogger;

//TODO add double command checking
class ControllableImpl implements Controllable {

	private static final Logger log = getLogger(ControllableImpl.class);

	@Inject
	private CounterSenderService senderService;

	@Inject
	private CounterReceiverService receiverService;

	@Inject
	private SorterService sorterService;

	//TODO move to module
	private ExecutorService senderServiceExecutor = newSingleThreadExecutor(
		new BasicThreadFactory.Builder().namingPattern("CounterSenderService-%s").build()
	);

	//TODO move to module
	private ExecutorService receiverServiceExecutor = newSingleThreadExecutor(
		new BasicThreadFactory.Builder().namingPattern("CounterReceiverService-%s").build()
	);

	@Override
	public void init() {
		log.info("init counter sender");
		senderService.init();
		receiverService.startUp();
		senderServiceExecutor.execute(senderService);
		receiverServiceExecutor.execute(receiverService);
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
		receiverService.shutDown();
		sorterService.exit();
		senderServiceExecutor.shutdown();
		receiverServiceExecutor.shutdown();
	}
}
