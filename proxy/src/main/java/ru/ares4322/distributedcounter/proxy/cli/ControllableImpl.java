package ru.ares4322.distributedcounter.proxy.cli;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.cli.Controllable;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.receiver.ReceiverService;
import ru.ares4322.distributedcounter.common.sender.SenderService;
import ru.ares4322.distributedcounter.proxy.Echo;
import ru.ares4322.distributedcounter.proxy.Initiator;

import javax.inject.Inject;

import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.slf4j.LoggerFactory.getLogger;

class ControllableImpl implements Controllable {

	private static final Logger log = getLogger(ControllableImpl.class);

	private final SenderService echoSenderService;
	private final SenderService initiatorSenderService;
	private final ReceiverService echoReceiverService;
	private final ReceiverService initiatorReceiverService;
	private final ConnectionPool echoConnectionPool;
	private final ConnectionPool initiatorConnectionPool;

	@Inject
	public ControllableImpl(
		@Echo SenderService echoSenderService,
		@Initiator SenderService initiatorSenderService,
		@Echo ReceiverService echoReceiverService,
		@Initiator ReceiverService initiatorReceiverService,
		@Echo ConnectionPool echoConnectionPool,
		@Initiator ConnectionPool initiatorConnectionPool
	) {
		this.echoSenderService = echoSenderService;
		this.initiatorSenderService = initiatorSenderService;
		this.echoReceiverService = echoReceiverService;
		this.initiatorReceiverService = initiatorReceiverService;
		this.echoConnectionPool = echoConnectionPool;
		this.initiatorConnectionPool = initiatorConnectionPool;
	}

	@Override
	public void init() {
		log.info("proxy init...");
		echoSenderService.init();
		initiatorSenderService.init();
		echoReceiverService.init();
		initiatorReceiverService.init();
		echoConnectionPool.init();
		initiatorConnectionPool.init();
		log.info("proxy inited");
	}

	@Override
	public void start() {
		log.info("proxy start...");
		echoSenderService.startUp();
		initiatorSenderService.startUp();
		echoReceiverService.startUp();
		initiatorReceiverService.startUp();
		echoConnectionPool.start();
		initiatorConnectionPool.start();
		log.info("proxy started");
	}

	@Override
	public void stop() {
		log.info("proxy stop is not supported");
	}

	@Override
	public void exit() {
		log.info("proxy exit...");
		echoSenderService.shutDown();
		initiatorSenderService.shutDown();
		echoReceiverService.shutDown();
		initiatorReceiverService.shutDown();
		closeQuietly(echoConnectionPool);
		closeQuietly(initiatorConnectionPool);
		log.info("proxy exited");
	}
}
