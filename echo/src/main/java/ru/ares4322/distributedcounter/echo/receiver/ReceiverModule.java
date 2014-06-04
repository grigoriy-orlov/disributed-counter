package ru.ares4322.distributedcounter.echo.receiver;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.domain.Packet;
import ru.ares4322.distributedcounter.common.receiver.ReceiverConfig;
import ru.ares4322.distributedcounter.common.receiver.ReceiverExecutor;
import ru.ares4322.distributedcounter.common.receiver.ReceiverService;
import ru.ares4322.distributedcounter.common.receiver.ReceiverTask;
import ru.ares4322.distributedcounter.common.receiver.common.ReceiverServiceImpl;
import ru.ares4322.distributedcounter.common.receiver.common.ReceiverTaskImpl;
import ru.ares4322.distributedcounter.echo.ReceiverToSenderQueue;

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
	public ExecutorService getReceiverExecutor(ReceiverConfig config) {
		if (config.getThreads() > 1) {
			return newFixedThreadPool(
				config.getThreads() - 1,
				new BasicThreadFactory.Builder().namingPattern("ReceiverTask-%s").daemon(true).build()
			);
		} else {
			return sameThreadExecutor();
		}
	}

	@Provides
	public ReceiverTask getReceiverTask(
		@ReceiverToSenderQueue BlockingQueue<Packet> outputQueue
	) {
		return new ReceiverTaskImpl(outputQueue);
	}

	@Provides
	@Singleton
	public ReceiverService getReceiverService(
		ReceiverConfig config,
		@ReceiverExecutor ExecutorService taskExecutor,
		Provider<ReceiverTask> taskProvider
	) {
		return new ReceiverServiceImpl(config, taskExecutor, taskProvider);
	}
}
