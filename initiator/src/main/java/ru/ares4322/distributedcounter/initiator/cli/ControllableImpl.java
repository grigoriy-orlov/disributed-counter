package ru.ares4322.distributedcounter.initiator.cli;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.cli.Controllable;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.receiver.CounterReceiverService;
import ru.ares4322.distributedcounter.common.sender.CounterSenderService;
import ru.ares4322.distributedcounter.common.sorter.SorterService;

import javax.inject.Inject;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

//TODO add double command checking
class ControllableImpl implements Controllable {

	private static final Logger log = getLogger(ControllableImpl.class);

	private final CounterSenderService senderService;
	private final CounterReceiverService receiverService;
	private final SorterService sorterService;
	private final ConnectionPool connectionPool;

	@Inject
	public ControllableImpl(
		CounterSenderService senderService,
		CounterReceiverService receiverService,
		SorterService sorterService,
		ConnectionPool connectionPool
	) {
		this.senderService = senderService;
		this.receiverService = receiverService;
		this.sorterService = sorterService;
		this.connectionPool = connectionPool;
	}

	@Override
	public void init() {
		log.info("init counter sender");
		senderService.init();
		receiverService.init();
		connectionPool.init();
	}

	@Override
	public void start() {
		log.info("startUp counter sender");
		senderService.startUp();
		receiverService.startUp();
		sorterService.startUp();
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
		sorterService.shutDown();
		try {
			connectionPool.close();
		} catch (IOException e) {
			log.error("connection pool closing error", e);
		}
	}
}
