package ru.ares4322.distributedcounter.initiator.sender;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.sender.Sender;
import ru.ares4322.distributedcounter.common.sender.SenderExecutor;
import ru.ares4322.distributedcounter.common.sender.SenderService;
import ru.ares4322.distributedcounter.common.sender.SenderTask;
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

public class SenderModule extends AbstractModule {

	private static final Logger log = getLogger(SenderModule.class);

	@Override
	protected void configure() {
		log.debug("start configure CounterSenderModule");

		binder()
			.bind(Sender.class)
			.to(SenderImpl.class)
			.in(SINGLETON);

		log.debug("finish configure CounterSenderModule");
	}

	@Provides
	@Singleton
	@SenderExecutor
	public ExecutorService getCounterSenderExecutor(InitiatorConfig config) {
		return newFixedThreadPool(
			config.getSenderThreads(),
			new BasicThreadFactory.Builder().namingPattern("CounterSenderTask-%s").build()
		);
	}

	@Provides
	public SenderTask getCounterSenderTask(
		Sender sender
	) {
		return new SenderTaskImpl(sender);
	}

	@Provides
	@Singleton
	public SenderService getCounterSenderService(
		Provider<SenderTask> counterSenderTaskProvider,
		@SenderExecutor ExecutorService taskExecutor,
		@InitiatorToSenderQueue BlockingQueue<Integer> inputQueue,
		@SenderToSorterQueue BlockingQueue<Integer> outputQueue
	) {
		return new SenderServiceImpl(
			counterSenderTaskProvider,
			taskExecutor,
			inputQueue,
			outputQueue
		);
	}

}
