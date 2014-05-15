package ru.ares4322.distributedcounter.initiator;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.CounterSenderExecutor;
import ru.ares4322.distributedcounter.common.CounterSenderService;
import ru.ares4322.distributedcounter.common.CounterSenderTask;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.newBufferedWriter;
import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.slf4j.LoggerFactory.getLogger;

@Singleton
public class CounterSenderServiceImpl implements CounterSenderService {

	private static final Logger log = getLogger(CounterSenderServiceImpl.class);

	@Inject
	private InitiatorConfig config;

	@Inject
	private Provider<CounterSenderTaskImpl> counterSenderTaskProvider;

	@Inject
	@CounterSenderExecutor
	private ExecutorService executor;

	private AtomicInteger counter = new AtomicInteger();
	private BufferedWriter writer;

	private Integer maxCounter;

	//maybe two locks
	ReentrantLock lock = new ReentrantLock(true);

	@Override
	public void init(){
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

		try {
			if (null == writer) {
				writer = newBufferedWriter(config.getSenderFilePath(), forName("UTF-8"));
			}
		} catch (IOException e) {
			log.error("counter receiver init selector error", e);
			throw new IllegalStateException("counter receiver starting error");
		}
	}

	@Override
	public void shutDown() {
		log.debug("shutDown");
		if (lock.isLocked()) {
			//FIXME not atomicity
			lock.unlock();
		}

		if (null != executor) {
			executor.shutdown();
		}
		closeQuietly(writer);
	}

	//TODO beautify
	@Override
	public void run() {
		log.debug("run");
		final int tasks = 3;
		int t = tasks;
		ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<>(executor, new ArrayBlockingQueue<Future<Integer>>(tasks));
		while (true) {
			lock.lock();
			try {
				int next = counter.getAndIncrement();
				if (maxCounter != null && next > maxCounter) {
					break;
				}
				log.debug("get counter (value={}) and startUp task", next);
				CounterSenderTask task = counterSenderTaskProvider.get();
				task.setCounter(next);
				task.setWriter(writer);
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
		while(lock.tryLock() != true);
		log.debug("finish suspend");
	}
}
