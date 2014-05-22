package ru.ares4322.distributedcounter.initiator;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.SorterExecutor;
import ru.ares4322.distributedcounter.common.SorterService;
import ru.ares4322.distributedcounter.common.SorterTask;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;

import static org.slf4j.LoggerFactory.getLogger;

//TODO maybe remove queue
@Singleton
public class SorterServiceImpl implements SorterService {

	private static final Logger log = getLogger(SorterServiceImpl.class);

	@Inject
	private Provider<SorterTask> sorterTaskProvider;

	@Inject
	@SorterExecutor
	private ExecutorService executor;

	@Override
	public void start() {
		log.debug("start");

		//TODO move to module
		executor.execute(sorterTaskProvider.get());
	}

	@Override
	public void exit() {
		log.debug("destroy");

		sorterTaskProvider.get().exit();
		executor.shutdown();
	}
}
