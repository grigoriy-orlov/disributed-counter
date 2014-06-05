package ru.ares4322.distributedcounter.echo.cli;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.cli.Controllable;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.receiver.ReceiverService;
import ru.ares4322.distributedcounter.common.sender.SenderService;
import ru.ares4322.distributedcounter.common.sorter.ReceiverWriter;
import ru.ares4322.distributedcounter.common.sorter.SorterService;

import javax.inject.Inject;
import java.io.Writer;

import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.slf4j.LoggerFactory.getLogger;

class ControllableImpl implements Controllable {

	private static final Logger log = getLogger(ControllableImpl.class);

	private final SenderService senderService;
	private final ReceiverService receiverService;
	private final SorterService sorterService;
	private final ConnectionPool connectionPool;
	private final Writer receiverWriter;

	@Inject
	public ControllableImpl(
		SenderService senderService,
		ReceiverService receiverService,
		SorterService sorterService,
		ConnectionPool connectionPool,
		@ReceiverWriter Writer receiverWriter
	) {
		this.senderService = senderService;
		this.receiverService = receiverService;
		this.sorterService = sorterService;
		this.connectionPool = connectionPool;
		this.receiverWriter = receiverWriter;
	}

	@Override
	public void init() {
		log.info("init echo...");
		senderService.init();
		receiverService.init();
		connectionPool.init();
		log.info("echo inited");
	}

	@Override
	public void start() {
		log.info("start echo...");
		senderService.startUp();
		receiverService.startUp();
		sorterService.startUp();
		connectionPool.start();
		log.info("echo started");
	}

	@Override
	public void stop() {
		log.info("echo stop not supported");
	}

	@Override
	public void exit() {
		log.info("echo exit...");
		senderService.shutDown();
		receiverService.shutDown();
		sorterService.shutDown();
		closeQuietly(connectionPool);
		closeQuietly(receiverWriter);
		log.info("echo exited");
	}
}
