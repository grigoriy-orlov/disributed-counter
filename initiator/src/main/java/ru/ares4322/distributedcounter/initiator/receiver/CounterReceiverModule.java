package ru.ares4322.distributedcounter.initiator.receiver;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.receiver.CounterReceiverExecutor;
import ru.ares4322.distributedcounter.common.receiver.CounterReceiverQueue;
import ru.ares4322.distributedcounter.common.receiver.CounterReceiverService;
import ru.ares4322.distributedcounter.common.receiver.CounterReceiverTask;
import ru.ares4322.distributedcounter.common.sorter.ReceiverWriter;
import ru.ares4322.distributedcounter.initiator.cfg.InitiatorConfig;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.Writer;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import static com.google.common.base.Throwables.propagate;
import static com.google.common.collect.Queues.newConcurrentLinkedQueue;
import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor;
import static com.google.inject.Scopes.SINGLETON;
import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.newBufferedWriter;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.slf4j.LoggerFactory.getLogger;

public class CounterReceiverModule extends AbstractModule {

	private static final Logger log = getLogger(CounterReceiverModule.class);

	@Override
	protected void configure() {
		log.debug("start configure CounterReceiverModule");

		binder()
			.bind(CounterReceiverService.class)
			.to(CounterReceiverServiceImpl.class)
			.in(SINGLETON);

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
	public CounterReceiverTask getCounterReceiverTask(
		@CounterReceiverQueue Queue<Integer> queue
	) {
		return new CounterReceiverTaskImpl(queue);
	}

	@Provides
	@Singleton
	@CounterReceiverQueue
	public Queue<Integer> getQueue() {
		return newConcurrentLinkedQueue();
	}

	@Provides
	@Singleton
	@ReceiverWriter
	public Writer getWriter(InitiatorConfig config) {
		//FIXME
		try {
			return newBufferedWriter(config.getReceiverFilePath(), forName("UTF-8"));
		} catch (IOException e) {
			log.error("receiver writer creation error", e);
			propagate(e);
			return null;
		}
	}
}
