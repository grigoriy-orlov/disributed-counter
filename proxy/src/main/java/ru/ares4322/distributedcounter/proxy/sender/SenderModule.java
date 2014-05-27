package ru.ares4322.distributedcounter.proxy.sender;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.domain.Packet;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.sender.Sender;
import ru.ares4322.distributedcounter.common.sender.SenderConfig;
import ru.ares4322.distributedcounter.common.sender.SenderService;
import ru.ares4322.distributedcounter.common.sender.SenderTask;
import ru.ares4322.distributedcounter.common.sender.common.SenderImpl;
import ru.ares4322.distributedcounter.common.sender.common.SenderServiceImpl;
import ru.ares4322.distributedcounter.common.sender.common.SenderTaskImpl;
import ru.ares4322.distributedcounter.proxy.*;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.slf4j.LoggerFactory.getLogger;

public class SenderModule extends AbstractModule {

	private static final Logger log = getLogger(SenderModule.class);

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	@Echo
	public Sender getEchoSender(
		@Echo ConnectionPool pool
	) {
		return new SenderImpl(pool);
	}

	@Provides
	@Singleton
	@EchoSenderExecutor
	public ExecutorService getEchoSenderExecutor(
		@Echo SenderConfig config
	) {
		return newFixedThreadPool(
			config.getThreads(),
			new BasicThreadFactory.Builder().namingPattern("EchoSenderTask-%s").build()
		);
	}

	@Provides
	@Echo
	public SenderTask getEchoSenderTask(
		@Echo Sender sender
	) {
		return new SenderTaskImpl(sender);
	}

	@Provides
	@Singleton
	@Echo
	public SenderService getEchoSenderService(
		@Echo Provider<SenderTask> counterSenderTaskProvider,
		@EchoSenderExecutor ExecutorService taskExecutor,
		@InitiatorReceiverToEchoSenderQueue BlockingQueue<Packet> inputQueue,
		@BlackHoleQueue BlockingQueue<Packet> outputQueue
	) {
		return new SenderServiceImpl(
			counterSenderTaskProvider,
			taskExecutor,
			inputQueue,
			outputQueue
		);
	}


	@Provides
	@Singleton
	@Initiator
	public Sender getInitiatorSender(
		@Initiator ConnectionPool pool
	) {
		return new SenderImpl(pool);
	}

	@Provides
	@Singleton
	@InitiatorSenderExecutor
	public ExecutorService getInitiatorSenderExecutor(
		@Initiator SenderConfig config
	) {
		return newFixedThreadPool(
			config.getThreads(),
			new BasicThreadFactory.Builder().namingPattern("InitiatorSenderTask-%s").build()
		);
	}

	@Provides
	@Initiator
	public SenderTask getInitiatorSenderTask(
		@Initiator Sender sender
	) {
		return new SenderTaskImpl(sender);
	}

	@Provides
	@Singleton
	@Initiator
	public SenderService getInitiatorSenderService(
		@Initiator Provider<SenderTask> counterSenderTaskProvider,
		@InitiatorSenderExecutor ExecutorService taskExecutor,
		@EchoReceiverToInitiatorSenderQueue BlockingQueue<Packet> inputQueue,
		@BlackHoleQueue BlockingQueue<Packet> outputQueue
	) {
		return new SenderServiceImpl(
			counterSenderTaskProvider,
			taskExecutor,
			inputQueue,
			outputQueue
		);
	}

}
