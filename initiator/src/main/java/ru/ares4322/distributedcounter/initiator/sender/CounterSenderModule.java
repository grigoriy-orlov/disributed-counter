package ru.ares4322.distributedcounter.initiator.sender;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.sender.CounterSender;
import ru.ares4322.distributedcounter.common.sender.CounterSenderExecutor;
import ru.ares4322.distributedcounter.common.sender.CounterSenderService;
import ru.ares4322.distributedcounter.common.sender.CounterSenderTask;
import ru.ares4322.distributedcounter.initiator.InitiatorToSenderQueue;
import ru.ares4322.distributedcounter.initiator.SenderToSorterQueue;
import ru.ares4322.distributedcounter.initiator.cfg.InitiatorConfig;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import static com.google.inject.Scopes.SINGLETON;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.slf4j.LoggerFactory.getLogger;

public class CounterSenderModule extends AbstractModule {

	private static final Logger log = getLogger(CounterSenderModule.class);

	@Override
	protected void configure() {
		log.debug("start configure CounterSenderModule");

		binder()
			.bind(CounterSender.class)
			.to(CounterSenderImpl.class)
			.in(SINGLETON);

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
	public CounterSenderTask getCounterSenderTask(
		CounterSender sender
	) {
		return new CounterSenderTaskImpl(sender);
	}

	@Provides
	@Singleton
	public CounterSenderService getCounterSenderService(
		Provider<CounterSenderTask> counterSenderTaskProvider,
		@CounterSenderExecutor ExecutorService taskExecutor,
		@InitiatorToSenderQueue BlockingQueue<Integer> inputQueue,
		@SenderToSorterQueue BlockingQueue<Integer> outputQueue
	) {
		return new CounterSenderServiceImpl(
			counterSenderTaskProvider,
			taskExecutor,
			inputQueue,
			outputQueue
		);
	}

}
