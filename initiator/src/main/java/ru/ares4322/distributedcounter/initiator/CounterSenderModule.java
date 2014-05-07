package ru.ares4322.distributedcounter.initiator;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.CounterSender;
import ru.ares4322.distributedcounter.common.CounterSenderExecutor;
import ru.ares4322.distributedcounter.common.CounterSenderService;
import ru.ares4322.distributedcounter.common.CounterSenderTask;

import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.slf4j.LoggerFactory.getLogger;

public class CounterSenderModule extends AbstractModule {

	private static final Logger log = getLogger(CounterSenderModule.class);

	@Override
	protected void configure() {
		log.debug("start configure CounterSenderModule");

		binder().bind(CounterSenderService.class).to(CounterSenderServiceImpl.class);
		binder().bind(CounterSender.class).to(CounterSenderImpl.class);

		log.debug("finish configure CounterSenderModule");
	}

	@Provides
	@Singleton
	@CounterSenderExecutor
	public ExecutorService getCounterSenderExecutor(InitiatorConfig config) {
		return newFixedThreadPool(
			config.getSenderThreads(),
			new BasicThreadFactory.Builder().namingPattern("CounterSenderTask-%s").build()
		);
	}

	@Provides
	public CounterSenderTask getCounterSenderTask() {
		return new CounterSenderTaskImpl();
	}
}
