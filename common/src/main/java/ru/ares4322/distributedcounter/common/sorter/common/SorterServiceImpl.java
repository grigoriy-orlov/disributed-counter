package ru.ares4322.distributedcounter.common.sorter.common;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.sorter.SorterService;
import ru.ares4322.distributedcounter.common.sorter.SorterTask;

import javax.inject.Provider;
import java.util.concurrent.ExecutorService;

import static org.slf4j.LoggerFactory.getLogger;

//TODO maybe remove queue
public class SorterServiceImpl implements SorterService {

	private static final Logger log = getLogger(SorterServiceImpl.class);

	private final Provider<SorterTask> sorterTaskProvider;
	private final ExecutorService taskExecutor;

	public SorterServiceImpl(
		Provider<SorterTask> sorterTaskProvider,
		ExecutorService taskExecutor
	) {
		this.sorterTaskProvider = sorterTaskProvider;
		this.taskExecutor = taskExecutor;
	}

	@Override
	public void startUp() {
		log.debug("start");

		//TODO move to module
		taskExecutor.execute(sorterTaskProvider.get());
	}

	@Override
	public void shutDown() {
		log.debug("destroy");

		sorterTaskProvider.get().exit();
		taskExecutor.shutdown();
	}
}
