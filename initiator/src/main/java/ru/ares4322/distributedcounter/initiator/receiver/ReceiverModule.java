package ru.ares4322.distributedcounter.initiator.receiver;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.receiver.ReceiverExecutor;
import ru.ares4322.distributedcounter.common.receiver.ReceiverService;
import ru.ares4322.distributedcounter.common.receiver.ReceiverTask;
import ru.ares4322.distributedcounter.initiator.ReceiverToSorterQueue;
import ru.ares4322.distributedcounter.initiator.cfg.InitiatorConfig;

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
	@ReceiverExecutor
	public ExecutorService getCounterReceiverExecutor(InitiatorConfig config) {
		if (config.getReceiverThreads() > 1) {
			return newFixedThreadPool(
				config.getReceiverThreads() - 1,
				new BasicThreadFactory.Builder().namingPattern("CounterReceiverTask-%s").build()
			);
		} else {
			return sameThreadExecutor();
		}
	}

	@Provides
	public ReceiverTask getCounterReceiverTask(
		@ReceiverToSorterQueue BlockingQueue<Integer> outputQueue
	) {
		return new ReceiverTaskImpl(outputQueue);
	}

	@Provides
	@Singleton
	public ReceiverService getCounterReceiverService(
		InitiatorConfig config,
		@ReceiverExecutor ExecutorService taskExecutor,
		Provider<ReceiverTask> taskProvider
	) {
		return new ReceiverServiceImpl(config, taskExecutor, taskProvider);
	}
}