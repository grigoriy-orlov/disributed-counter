package ru.ares4322.distributedcounter.initiator.sender;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.sender.CounterSender;
import ru.ares4322.distributedcounter.common.sender.CounterSenderExecutor;
import ru.ares4322.distributedcounter.common.sender.CounterSenderService;
import ru.ares4322.distributedcounter.common.sender.CounterSenderTask;
import ru.ares4322.distributedcounter.common.sorter.SenderWriter;
import ru.ares4322.distributedcounter.initiator.cfg.InitiatorConfig;

import javax.inject.Singleton;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import static com.google.common.base.Throwables.propagate;
import static com.google.inject.Scopes.SINGLETON;
import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.newBufferedWriter;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.slf4j.LoggerFactory.getLogger;

public class CounterSenderModule extends AbstractModule {

	private static final Logger log = getLogger(CounterSenderModule.class);

	@Override
	protected void configure() {
		log.debug("start configure CounterSenderModule");

		binder()
			.bind(CounterSenderService.class)
			.to(CounterSenderServiceImpl.class)
			.in(SINGLETON);

		binder()
			.bind(CounterSender.class)
			.to(CounterSenderImpl.class)
			.in(SINGLETON);;

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
		CounterSender sender,
		@SenderWriter BufferedWriter writer
	) {
		CounterSenderTaskImpl counterSenderTask = new CounterSenderTaskImpl(sender, writer);
		return counterSenderTask;
	}

	@Provides
	@Singleton
	@SenderWriter
	public BufferedWriter getWriter(InitiatorConfig config) {
		try {
			return newBufferedWriter(config.getSenderFilePath(), forName("UTF-8"));
		} catch (IOException e) {
			log.error("writer creation error", e);
			propagate(e);
			return null;
		}
	}
}
