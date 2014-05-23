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

	private final Provider<SorterTask> sorterTaskProvider;
	private final ExecutorService executor;

	@Inject
	public SorterServiceImpl(
		Provider<SorterTask> sorterTaskProvider,
		@SorterExecutor ExecutorService executor
	) {
		this.sorterTaskProvider = sorterTaskProvider;
		this.executor = executor;
	}

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
