package ru.ares4322.distributedcounter.initiator.sorter;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.sorter.SorterExecutor;
import ru.ares4322.distributedcounter.common.sorter.SorterService;
import ru.ares4322.distributedcounter.common.sorter.SorterTask;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.ExecutorService;

import static org.slf4j.LoggerFactory.getLogger;

//TODO maybe remove queue
class SorterServiceImpl implements SorterService {

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
