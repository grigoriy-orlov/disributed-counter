package ru.ares4322.distributedcounter.initiator.cli;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.cli.Controllable;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.receiver.ReceiverService;
import ru.ares4322.distributedcounter.common.sender.SenderService;
import ru.ares4322.distributedcounter.common.sorter.SorterService;
import ru.ares4322.distributedcounter.initiator.ReceiverSorter;
import ru.ares4322.distributedcounter.initiator.SenderSorter;
import ru.ares4322.distributedcounter.initiator.initiator.InitiatorService;

import javax.inject.Inject;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

class ControllableImpl implements Controllable {

	private static final Logger log = getLogger(ControllableImpl.class);

	private InitiatorService initiatorService;
	private final SenderService senderService;
	private final ReceiverService receiverService;
	private SorterService senderSorterService;
	private final SorterService receiverSorterService;
	private final ConnectionPool connectionPool;

	@Inject
	public ControllableImpl(
		InitiatorService initiatorService,
		SenderService senderService,
		ReceiverService receiverService,
		@SenderSorter SorterService senderSorterService,
		@ReceiverSorter SorterService receiverSorterService,
		ConnectionPool connectionPool
	) {
		this.initiatorService = initiatorService;
		this.senderService = senderService;
		this.receiverService = receiverService;
		this.senderSorterService = senderSorterService;
		this.receiverSorterService = receiverSorterService;
		this.connectionPool = connectionPool;
	}

	@Override
	public void init() {
		log.info("init counter sender");
		initiatorService.init();
		senderService.init();
		receiverService.init();
		connectionPool.init();
	}

	@Override
	public void start() {
		log.info("startUp counter sender");
		senderService.startUp();
		receiverService.startUp();
		senderSorterService.startUp();
		receiverSorterService.startUp();
		initiatorService.startUp();
	}

	@Override
	public void stop() {
		log.info("stop counter sender");
		initiatorService.stop();
	}

	@Override
	public void exit() {
		log.info("exit counter sender");
		initiatorService.shutDown();
		senderService.shutDown();
		receiverService.shutDown();
		receiverSorterService.shutDown();
		senderSorterService.shutDown();
		try {
			connectionPool.close();
		} catch (IOException e) {
			log.error("connection pool closing error", e);
		}
	}
}
