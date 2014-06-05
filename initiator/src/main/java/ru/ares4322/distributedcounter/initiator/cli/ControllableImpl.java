package ru.ares4322.distributedcounter.initiator.cli;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.cli.Controllable;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.receiver.ReceiverService;
import ru.ares4322.distributedcounter.common.sender.SenderService;
import ru.ares4322.distributedcounter.common.sorter.ReceiverWriter;
import ru.ares4322.distributedcounter.common.sorter.SenderWriter;
import ru.ares4322.distributedcounter.common.sorter.SorterService;
import ru.ares4322.distributedcounter.initiator.Exit;
import ru.ares4322.distributedcounter.initiator.ReceiverSorter;
import ru.ares4322.distributedcounter.initiator.SenderSorter;
import ru.ares4322.distributedcounter.initiator.initiator.ExitService;
import ru.ares4322.distributedcounter.initiator.initiator.InitiatorService;

import javax.inject.Inject;
import java.io.Writer;
import java.util.concurrent.CountDownLatch;

import static com.google.common.util.concurrent.Uninterruptibles.awaitUninterruptibly;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.slf4j.LoggerFactory.getLogger;

class ControllableImpl implements Controllable {

	private static final Logger log = getLogger(ControllableImpl.class);

	private InitiatorService initiatorService;
	private final SenderService senderService;
	private final ReceiverService receiverService;
	private SorterService senderSorterService;
	private final SorterService receiverSorterService;
	private final ExitService exitService;
	private final ConnectionPool connectionPool;
	private final Writer senderWriter;
	private final Writer receiverWriter;
	private final CountDownLatch exitLatch;

	@Inject
	public ControllableImpl(
		InitiatorService initiatorService,
		SenderService senderService,
		ReceiverService receiverService,
		@SenderSorter SorterService senderSorterService,
		@ReceiverSorter SorterService receiverSorterService,
		ExitService exitService,
		ConnectionPool connectionPool,
		@SenderWriter Writer senderWriter,
		@ReceiverWriter Writer receiverWriter,
		@Exit CountDownLatch exitLatch
	) {
		this.initiatorService = initiatorService;
		this.senderService = senderService;
		this.receiverService = receiverService;
		this.senderSorterService = senderSorterService;
		this.receiverSorterService = receiverSorterService;
		this.exitService = exitService;
		this.connectionPool = connectionPool;
		this.senderWriter = senderWriter;
		this.receiverWriter = receiverWriter;
		this.exitLatch = exitLatch;
	}

	@Override
	public void init() {
		log.info("init initiator...");
		initiatorService.init();
		senderService.init();
		receiverService.init();
		connectionPool.init();
		exitService.init();
		log.info("initiator inited");
	}

	@Override
	public void start() {
		log.info("start initiator...");
		senderService.startUp();
		receiverService.startUp();
		senderSorterService.startUp();
		receiverSorterService.startUp();
		connectionPool.start();
		initiatorService.startUp();
		exitService.startUp();
		log.info("initiator started");
	}

	@Override
	public void stop() {
		log.info("stop initiator...");
		initiatorService.stop();
		log.info("initiator stopped");
	}

	@Override
	public void exit() {
		log.info("exit initiator...");
		initiatorService.shutDown();
		awaitUninterruptibly(exitLatch);
		sleepUninterruptibly(3, SECONDS);
		exitService.shutDown();
		senderService.shutDown();
		receiverService.shutDown();
		receiverSorterService.shutDown();
		senderSorterService.shutDown();
		closeQuietly(connectionPool);
		closeQuietly(senderWriter);
		closeQuietly(receiverWriter);
		log.info("initiator exited");
	}
}
