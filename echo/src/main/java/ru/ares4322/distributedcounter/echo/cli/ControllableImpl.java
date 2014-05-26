package ru.ares4322.distributedcounter.echo.cli;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.cli.Controllable;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.receiver.ReceiverService;
import ru.ares4322.distributedcounter.common.sender.SenderService;
import ru.ares4322.distributedcounter.common.sorter.SorterService;

import javax.inject.Inject;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

class ControllableImpl implements Controllable {

	private static final Logger log = getLogger(ControllableImpl.class);

	private final SenderService senderService;
	private final ReceiverService receiverService;
	private final SorterService sorterService;
	private final ConnectionPool connectionPool;

	@Inject
	public ControllableImpl(
		SenderService senderService,
		ReceiverService receiverService,
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
