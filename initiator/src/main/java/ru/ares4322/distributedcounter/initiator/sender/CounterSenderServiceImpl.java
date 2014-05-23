package ru.ares4322.distributedcounter.initiator.sender;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.sender.CounterSenderExecutor;
import ru.ares4322.distributedcounter.common.sender.CounterSenderService;
import ru.ares4322.distributedcounter.common.sender.CounterSenderTask;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.slf4j.LoggerFactory.getLogger;

class CounterSenderServiceImpl implements CounterSenderService {

	private static final Logger log = getLogger(CounterSenderServiceImpl.class);

	private final Provider<CounterSenderTaskImpl> counterSenderTaskProvider;
	private final ExecutorService taskExecutor;
	//TODO move to module
	private final ExecutorService serviceExecutor = newSingleThreadExecutor(
		new BasicThreadFactory.Builder().namingPattern("CounterSenderService-%s").build()
	);

	private AtomicInteger counter = new AtomicInteger();
	private Integer maxCounter;
	//maybe two locks
	private ReentrantLock lock = new ReentrantLock(true);

	@Inject
	public CounterSenderServiceImpl(
		Provider<CounterSenderTaskImpl> counterSenderTaskProvider,
		@CounterSenderExecutor ExecutorService taskExecutor
	) {
		this.counterSenderTaskProvider = counterSenderTaskProvider;
		this.taskExecutor = taskExecutor;
	}

	@Override
	public void init() {
		log.debug("init");
		if (!lock.isLocked()) {
			//FIXME not atomicity
			lock.lock();
		}
	}

	@PostConstruct
	@Override
	public void startUp() {
		log.debug("startUp");
		if (lock.isLocked()) {
			//FIXME not atomicity
			lock.unlock();
		}
		serviceExecutor.execute(this);
	}

	@Override
	public void shutDown() {
		log.debug("shutDown");
		lock.lock();

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
			lock.lock();
			try {
				int next = counter.getAndIncrement();
				if (maxCounter != null && next > maxCounter) {
					break;
				}
				log.debug("get counter (value={}) and init task", next);
				CounterSenderTask task = counterSenderTaskProvider.get();
				task.setCounter(next);
				completionService.submit(task, 1);
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
			} finally {
				lock.unlock();
			}
		}
		log.debug("finish counter sending");
	}

	@Override
	public void setMaxCounter(Integer maxCounter) {
		this.maxCounter = maxCounter;
	}

	@Override
	public void suspend() {
		log.debug("start suspend");
		//FIXME not atomicity
		while (lock.tryLock() != true) ;
		log.debug("finish suspend");
	}
}
