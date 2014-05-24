package ru.ares4322.distributedcounter.common.sorter.common;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.sorter.SorterTask;
import ru.ares4322.distributedcounter.common.sorter.WriterTask;

import javax.inject.Provider;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import static org.slf4j.LoggerFactory.getLogger;

//TODO add dynamic arrays creation
public class SorterTaskImpl implements SorterTask {

	private static final Logger log = getLogger(SorterTaskImpl.class);

	private final BlockingQueue<Integer> inputQueue;
	private final ExecutorService executor;
	private final Provider<WriterTask> writerTaskProvider;

	//TODO move to config
	static int intervalLength = 10000;
	private int curIntervalCounter;
	private int nextIntervalCounter;
	private int curIntervalStartNum;
	private int nextIntervalStartNum = intervalLength;
	private volatile boolean inWork = true;

	public SorterTaskImpl(
		BlockingQueue<Integer> inputQueue,
		ExecutorService executor,
		Provider<WriterTask> writerTaskProvider
	) {
		this.inputQueue = inputQueue;
		this.executor = executor;
		this.writerTaskProvider = writerTaskProvider;
	}


	@Override
	public void run() {
		log.debug("run");
		Integer num;
		while (inWork) {
			try {
				if ((num = inputQueue.take()) != null) {
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
			//FIXME now number will be lost
			} catch (InterruptedException e) {
				log.error("input queue taking error", e);
				break;
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
