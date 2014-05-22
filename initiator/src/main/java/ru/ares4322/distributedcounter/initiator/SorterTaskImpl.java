package ru.ares4322.distributedcounter.initiator;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.CounterReceiverQueue;
import ru.ares4322.distributedcounter.common.SorterTask;
import ru.ares4322.distributedcounter.common.WriterExecutor;
import ru.ares4322.distributedcounter.common.WriterTask;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import static org.slf4j.LoggerFactory.getLogger;

//TODO add dynamic arrays creation
class SorterTaskImpl implements SorterTask {

	private static final Logger log = getLogger(SorterTaskImpl.class);

	@Inject
	@CounterReceiverQueue
	private Queue<Integer> queue;

	@Inject
	@WriterExecutor
	private ExecutorService executor;

	@Inject
	private Provider<WriterTask> writerTaskProvider;

	//TODO move to config
	static int intervalLength = 10000;

	private int curIntervalCounter;
	private int nextIntervalCounter;

	private int curIntervalStartNum;
	private int nextIntervalStartNum = intervalLength;

	private volatile boolean inWork = true;


	@Override
	public void run() {
		log.debug("run");
		Integer num;
		while (inWork) {
			if ((num = queue.poll()) != null) {
				if (num >= curIntervalStartNum && num < nextIntervalStartNum) {
					curIntervalCounter++;
					if (curIntervalCounter == intervalLength) {
						WriterTask task = writerTaskProvider.get();
						task.setInterval(curIntervalStartNum, intervalLength);
						executor.execute(task);

						curIntervalStartNum = nextIntervalStartNum;
						nextIntervalStartNum += intervalLength;
						curIntervalCounter = nextIntervalCounter;
						nextIntervalCounter = 0;
					}
				} else if (num >= nextIntervalStartNum && num < nextIntervalStartNum + intervalLength) {
					nextIntervalCounter++;
					if (nextIntervalCounter == intervalLength) {
						log.error("next array is full !!!");
					}
				} else {
					log.error("try to write to free array");
				}
			}
		}
		WriterTask curIntervalTask = writerTaskProvider.get();
		curIntervalTask.setInterval(curIntervalStartNum, curIntervalCounter);
		executor.execute(curIntervalTask);
		if (nextIntervalCounter > 0) {
			WriterTask nextIntervalTask = writerTaskProvider.get();
			nextIntervalTask.setInterval(nextIntervalStartNum, nextIntervalCounter);
			executor.execute(nextIntervalTask);
		}
		executor.shutdown();
	}

	@Override
	public void exit() {
		inWork = false;
	}
}
