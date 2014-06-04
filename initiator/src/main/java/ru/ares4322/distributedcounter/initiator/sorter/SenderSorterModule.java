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
import ru.ares4322.distributedcounter.initiator.SenderSorter;
import ru.ares4322.distributedcounter.initiator.SenderSorterExecutor;
import ru.ares4322.distributedcounter.initiator.SenderToSorterQueue;
import ru.ares4322.distributedcounter.initiator.SenderWriterExecutor;

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

public class SenderSorterModule extends AbstractModule {

	private static final Logger log = getLogger(SenderSorterModule.class);

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	@SenderWriter
	public Writer getWriter(
		@SenderWriter WriterConfig config
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
	@SenderSorter
	public WriterTask getReceiverWriterTask(
		@SenderWriter Writer writer
	) {
		return new WriterTaskImpl(writer);
	}

	@Provides
	@Singleton
	@SenderWriterExecutor
	public ExecutorService getReceiverWriterExecutor() {
		return newSingleThreadExecutor(
			new BasicThreadFactory.Builder().namingPattern("SenderWriterTask-%s").daemon(true).build()
		);
	}

	@Provides
	@Singleton
	@SenderSorter
	public SorterTask getSenderSorterTask(
		@SenderToSorterQueue BlockingQueue<Packet> inputQueue,
		@SenderWriterExecutor ExecutorService executor,
		@SenderSorter Provider<WriterTask> writerTaskProvider
	) {
		return new SorterTaskImpl(inputQueue, executor, writerTaskProvider);
	}

	@Provides
	@Singleton
	@SenderSorterExecutor
	public ExecutorService getSenderSorterExecutor() {
		return newSingleThreadExecutor(
			new BasicThreadFactory.Builder().namingPattern("SenderSorterTask-%s").daemon(true).build()
		);
	}

	@Provides
	@Singleton
	@SenderSorter
	public SorterService getSenderSorterService(
		@SenderSorter Provider<SorterTask> sorterTaskProvider,
		@SenderSorterExecutor ExecutorService taskExecutor
	) {
		return new SorterServiceImpl(sorterTaskProvider, taskExecutor);
	}
}
