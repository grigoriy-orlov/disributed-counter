package ru.ares4322.distributedcounter.initiator.sorter;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.domain.Packet;
import ru.ares4322.distributedcounter.common.sorter.*;
import ru.ares4322.distributedcounter.common.sorter.common.SorterServiceImpl;
import ru.ares4322.distributedcounter.common.sorter.common.SorterTaskImpl;
import ru.ares4322.distributedcounter.common.sorter.common.WriterTaskImpl;
import ru.ares4322.distributedcounter.initiator.ExitorToSorterQueue;
import ru.ares4322.distributedcounter.initiator.ReceiverSorter;
import ru.ares4322.distributedcounter.initiator.ReceiverSorterExecutor;
import ru.ares4322.distributedcounter.initiator.ReceiverWriterExecutor;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import static com.google.common.base.Throwables.propagate;
import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.newBufferedWriter;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.slf4j.LoggerFactory.getLogger;

public class ReceiverSorterModule extends AbstractModule {

	private static final Logger log = getLogger(ReceiverSorterModule.class);

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	@ReceiverWriter
	public Writer getWriter(
		@ReceiverWriter WriterConfig config
	) {
		try {
			return newBufferedWriter(config.getFilePath(), forName("UTF-8"));
		} catch (IOException e) {
			log.error("receiver writer creation error", e);
			propagate(e);
			return null;
		}
	}

	@Provides
	@Singleton
	@ReceiverSorter
	public WriterTask getReceiverWriterTask(
		@ReceiverWriter Writer writer
	) {
		return new WriterTaskImpl(writer);
	}

	@Provides
	@Singleton
	@ReceiverWriterExecutor
	public ExecutorService getReceiverWriterExecutor() {
		return newSingleThreadExecutor(
			new BasicThreadFactory.Builder().namingPattern("ReceiverWriterTask-%s").daemon(true).build()
		);
	}

	@Provides
	@Singleton
	@ReceiverSorter
	public SorterTask getReceiverSorterTask(
		@ExitorToSorterQueue BlockingQueue<Packet> inputQueue,
		@ReceiverWriterExecutor ExecutorService executor,
		@ReceiverSorter Provider<WriterTask> writerTaskProvider
	) {
		return new SorterTaskImpl(inputQueue, executor, writerTaskProvider);
	}

	@Provides
	@Singleton
	@ReceiverSorterExecutor
	public ExecutorService getReceiverSorterExecutor() {
		return newSingleThreadExecutor(
			new BasicThreadFactory.Builder().namingPattern("ReceiverSorterTask-%s").daemon(true).build()
		);
	}

	@Provides
	@Singleton
	@ReceiverSorter
	public SorterService getReceiverSorterService(
		@ReceiverSorter Provider<SorterTask> sorterTaskProvider,
		@ReceiverSorterExecutor ExecutorService taskExecutor
	) {
		return new SorterServiceImpl(sorterTaskProvider, taskExecutor);
	}
}
