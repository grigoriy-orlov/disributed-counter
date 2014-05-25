package ru.ares4322.distributedcounter.proxy.receiver;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.receiver.ReceiverConfig;
import ru.ares4322.distributedcounter.common.receiver.ReceiverService;
import ru.ares4322.distributedcounter.common.receiver.ReceiverTask;
import ru.ares4322.distributedcounter.common.receiver.common.ReceiverServiceImpl;
import ru.ares4322.distributedcounter.common.receiver.common.ReceiverTaskImpl;
import ru.ares4322.distributedcounter.proxy.Echo;
import ru.ares4322.distributedcounter.proxy.EchoReceiverToInitiatorSenderQueue;
import ru.ares4322.distributedcounter.proxy.Initiator;
import ru.ares4322.distributedcounter.proxy.InitiatorReceiverToEchoSenderQueue;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.slf4j.LoggerFactory.getLogger;

public class ReceiverModule extends AbstractModule {

	private static final Logger log = getLogger(ReceiverModule.class);

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	@EchoReceiverExecutor
	public ExecutorService getEchoReceiverExecutor(
		@Echo ReceiverConfig config
	) {
		if (config.getThreads() > 1) {
			return newFixedThreadPool(
				config.getThreads() - 1,
				new BasicThreadFactory.Builder().namingPattern("EchoReceiverTask-%s").build()
			);
		} else {
			return sameThreadExecutor();
		}
	}

	@Provides
	@Echo
	public ReceiverTask getEchoReceiverTask(
		@EchoReceiverToInitiatorSenderQueue BlockingQueue<Integer> outputQueue
	) {
		return new ReceiverTaskImpl(outputQueue);
	}

	@Provides
	@Singleton
	@Echo
	public ReceiverService getEchoReceiverService(
		@Echo ReceiverConfig config,
		@EchoReceiverExecutor ExecutorService taskExecutor,
		@Echo Provider<ReceiverTask> taskProvider
	) {
		return new ReceiverServiceImpl(config, taskExecutor, taskProvider);
	}

	@Provides
	@Singleton
	@InitiatorReceiverExecutor
	public ExecutorService getInitiatorReceiverExecutor(
		@Initiator ReceiverConfig config
	) {
		if (config.getThreads() > 1) {
			return newFixedThreadPool(
				config.getThreads() - 1,
				new BasicThreadFactory.Builder().namingPattern("InitiatorReceiverTask-%s").build()
			);
		} else {
			return sameThreadExecutor();
		}
	}

	@Provides
	@Initiator
	public ReceiverTask getInitiatorReceiverTask(
		@InitiatorReceiverToEchoSenderQueue BlockingQueue<Integer> outputQueue
	) {
		return new ReceiverTaskImpl(outputQueue);
	}

	@Provides
	@Singleton
	@Initiator
	public ReceiverService getInitiatorReceiverService(
		@Initiator ReceiverConfig config,
		@InitiatorReceiverExecutor ExecutorService taskExecutor,
		@Initiator Provider<ReceiverTask> taskProvider
	) {
		return new ReceiverServiceImpl(config, taskExecutor, taskProvider);
	}
}
