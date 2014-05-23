package ru.ares4322.distributedcounter.initiator.sorter;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.receiver.CounterReceiverQueue;
import ru.ares4322.distributedcounter.common.sorter.*;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.Writer;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import static com.google.inject.Scopes.SINGLETON;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.slf4j.LoggerFactory.getLogger;

public class SorterModule extends AbstractModule {

	private static final Logger log = getLogger(SorterModule.class);

	@Override
	protected void configure() {
		log.debug("start configure");

		binder()
			.bind(SorterService.class)
			.to(SorterServiceImpl.class)
			.in(SINGLETON);

		log.debug("finish configure");
	}

	@Provides
	@Singleton
	public SorterTask getSorterTask(
		@CounterReceiverQueue Queue<Integer> queue,
		@WriterExecutor ExecutorService executor,
		Provider<WriterTask> writerTaskProvider
	) {
		SorterTaskImpl sorterTask = new SorterTaskImpl(queue, executor, writerTaskProvider);
		return sorterTask;
	}

	@Provides
	@Singleton
	public WriterTask getWriterTask(
		@ReceiverWriter Writer writer
	) {
		WriterTaskImpl writerTask = new WriterTaskImpl(writer);
		return writerTask;
	}

	@Provides
	@Singleton
	@WriterExecutor
	public ExecutorService getWriterExecutor() {
		return newSingleThreadExecutor(
			new BasicThreadFactory.Builder().namingPattern("WriterTask-%s").build()
		);
	}

	@Provides
	@Singleton
	@SorterExecutor
	public ExecutorService getSorterExecutor() {
		return newSingleThreadExecutor(
			new BasicThreadFactory.Builder().namingPattern("SorterTask-%s").build()
		);
	}
}
