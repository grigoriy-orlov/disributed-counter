package ru.ares4322.distributedcounter.initiator;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.*;

import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.slf4j.LoggerFactory.getLogger;

public class SorterModule extends AbstractModule {

	private static final Logger log = getLogger(SorterModule.class);

	@Override
	protected void configure() {
		log.debug("start configure");

		binder().bind(SorterService.class).to(SorterServiceImpl.class);

		log.debug("finish configure");
	}

	@Provides
	@Singleton
	public SorterTask getSorterTask(Injector injector) {
		SorterTaskImpl sorterTask = new SorterTaskImpl();
		injector.injectMembers(sorterTask);
		return sorterTask;
	}

	@Provides
	@Singleton
	public WriterTask getWriterTask(Injector injector) {
		WriterTaskImpl writerTask = new WriterTaskImpl();
		injector.injectMembers(writerTask);
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
