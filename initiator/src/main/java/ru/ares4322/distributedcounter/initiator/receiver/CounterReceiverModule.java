package ru.ares4322.distributedcounter.initiator.receiver;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.receiver.CounterReceiverExecutor;
import ru.ares4322.distributedcounter.common.receiver.CounterReceiverService;
import ru.ares4322.distributedcounter.common.receiver.CounterReceiverTask;
import ru.ares4322.distributedcounter.initiator.ReceiverToSorterQueue;
import ru.ares4322.distributedcounter.initiator.cfg.InitiatorConfig;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.slf4j.LoggerFactory.getLogger;

public class CounterReceiverModule extends AbstractModule {

	private static final Logger log = getLogger(CounterReceiverModule.class);

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	@CounterReceiverExecutor
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
	public CounterReceiverTask getCounterReceiverTask(
		@ReceiverToSorterQueue BlockingQueue<Integer> outputQueue
	) {
		return new CounterReceiverTaskImpl(outputQueue);
	}

	@Provides
	@Singleton
	public CounterReceiverService getCounterReceiverService(
		InitiatorConfig config,
		@CounterReceiverExecutor ExecutorService taskExecutor,
		Provider<CounterReceiverTask> taskProvider
	) {
		return new CounterReceiverServiceImpl(config, taskExecutor, taskProvider);
	}
}
