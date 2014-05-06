package ru.ares4322.distributedcounter.initiator;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.CounterReceiverExecutor;
import ru.ares4322.distributedcounter.common.CounterReceiverService;
import ru.ares4322.distributedcounter.common.CounterReceiverTask;

import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;

import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.slf4j.LoggerFactory.getLogger;

public class CounterReceiverModule extends AbstractModule {

	private static final Logger log = getLogger(CounterReceiverModule.class);

	@Override
	protected void configure() {
		log.debug("start configure CounterReceiverModule");

		binder().bind(CounterReceiverService.class).to(CounterReceiverServiceImpl.class);

		log.debug("finish configure CounterReceiverModule");
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
	public CounterReceiverTask getCounterReceiverTask() {
		return new CounterReceiverTaskImpl();
	}
}
