package ru.ares4322.distributedcounter.initiator.sender;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.sender.CounterSenderService;
import ru.ares4322.distributedcounter.common.sender.CounterSenderTask;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.*;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.slf4j.LoggerFactory.getLogger;

class CounterSenderServiceImpl implements CounterSenderService {

	private static final Logger log = getLogger(CounterSenderServiceImpl.class);

	private final Provider<CounterSenderTask> counterSenderTaskProvider;
	private final ExecutorService taskExecutor;
	private final BlockingQueue<Integer> inputQueue;
	private final BlockingQueue<Integer> outputQueue;
	//TODO move to module
	private final ExecutorService serviceExecutor = newSingleThreadExecutor(
		new BasicThreadFactory.Builder().namingPattern("CounterSenderService-%s").build()
	);

	private boolean inWork;

	@Inject
	public CounterSenderServiceImpl(
		Provider<CounterSenderTask> counterSenderTaskProvider,
		ExecutorService taskExecutor,
		BlockingQueue<Integer> inputQueue,
		BlockingQueue<Integer> outputQueue
	) {
		this.counterSenderTaskProvider = counterSenderTaskProvider;
		this.taskExecutor = taskExecutor;
		this.inputQueue = inputQueue;
		this.outputQueue = outputQueue;
	}

	@Override
	public void init() {

	}

	@PostConstruct
	@Override
	public void startUp() {
		log.debug("startUp");
		inWork = true;
		serviceExecutor.execute(this);
	}

	@Override
	public void shutDown() {
		log.debug("shutDown");

		inWork = false;
		if (null != taskExecutor) {
			taskExecutor.shutdown();
		}
		if (null != serviceExecutor) {
			serviceExecutor.shutdown();
		}
		log.debug("shutDown complete");
	}

	//TODO beautify
	@Override
	public void run() {
		log.debug("run");
		final int tasks = 3;
		int t = tasks;
		ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<>(taskExecutor, new ArrayBlockingQueue<Future<Integer>>(tasks));
		while (true) {
			Integer next = null;
			try {
				next = inputQueue.take();
			} catch (InterruptedException e) {
				log.error("queue taking error, once again", e);
			}
			if (!inWork) {
				break;
			}
			if (next == null) {
				continue;
			}
			log.debug("get counter (value={}) and init task", next);
			CounterSenderTask task = counterSenderTaskProvider.get();
			task.setCounter(next);
			completionService.submit(task, 1);
			try {
				outputQueue.put(next);
				//FIXME now number will be lost
			} catch (InterruptedException e) {
				log.error("output queue putting error", e);
				break;
			}
			t--;
			if (t == 0) {
				try {
					completionService.take().get();
					t++;
				} catch (Exception e) {
					log.error("task taking error", e);
					//TODO right?
				}
			}
		}
		log.debug("finish counter sending");
	}

}
