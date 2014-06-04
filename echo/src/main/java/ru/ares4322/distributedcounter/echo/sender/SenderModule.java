package ru.ares4322.distributedcounter.echo.sender;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.domain.Packet;
import ru.ares4322.distributedcounter.common.sender.*;
import ru.ares4322.distributedcounter.common.sender.common.SenderImpl;
import ru.ares4322.distributedcounter.common.sender.common.SenderServiceImpl;
import ru.ares4322.distributedcounter.common.sender.common.SenderTaskImpl;
import ru.ares4322.distributedcounter.echo.ReceiverToSenderQueue;
import ru.ares4322.distributedcounter.echo.SenderToSorterQueue;

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
		log.debug("start configure SenderModule");

		binder()
			.bind(Sender.class)
			.to(SenderImpl.class)
			.in(SINGLETON);

		log.debug("finish configure SenderModule");
	}

	@Provides
	@Singleton
	@SenderExecutor
	public ExecutorService getSenderExecutor(SenderConfig config) {
		return newFixedThreadPool(
			config.getThreads(),
			new BasicThreadFactory.Builder().namingPattern("SenderTask-%s").daemon(true).build()
		);
	}

	@Provides
	public SenderTask getSenderTask(
		Sender sender
	) {
		return new SenderTaskImpl(sender);
	}

	@Provides
	@Singleton
	public SenderService getSenderService(
		Provider<SenderTask> senderTaskProvider,
		@SenderExecutor ExecutorService taskExecutor,
		@ReceiverToSenderQueue BlockingQueue<Packet> inputQueue,
		@SenderToSorterQueue BlockingQueue<Packet> outputQueue
	) {
		return new SenderServiceImpl(
			senderTaskProvider,
			taskExecutor,
			inputQueue,
			outputQueue
		);
	}

}
