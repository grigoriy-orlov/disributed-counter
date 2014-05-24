package ru.ares4322.distributedcounter.initiator.initiator;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.slf4j.LoggerFactory.getLogger;

class InitiatorServiceImpl implements InitiatorService {

	private static final Logger log = getLogger(InitiatorServiceImpl.class);

	private final BlockingQueue<Integer> outputQueue;
	//TODO move to module
	private final ExecutorService serviceExecutor = newSingleThreadExecutor(
		new BasicThreadFactory.Builder().namingPattern("InitiatorService-%s").build()
	);

	//TODO do just Integer
	private AtomicInteger counter = new AtomicInteger();
	private Integer maxCounter;
	private ReentrantLock lock = new ReentrantLock(true);

	public InitiatorServiceImpl(BlockingQueue<Integer> outputQueue) {
		this.outputQueue = outputQueue;
	}

	@Override
	public void init() {
		log.debug("init");
		if (!lock.isLocked()) {
			//FIXME not atomicity
			lock.lock();
		}
	}

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
	public void stop() {
		log.debug("start suspend");
		//FIXME not atomicity
		while (lock.tryLock() != true) ;
		log.debug("finish suspend");
	}

	@Override
	public void shutDown() {
		log.debug("shutDown");
		lock.lock();

		if (null != serviceExecutor) {
			serviceExecutor.shutdown();
		}
		log.debug("shutDown complete");
	}


	@Override
	public void run() {
		log.debug("run");
		while (true) {
			lock.lock();
			try {
				int next = counter.getAndIncrement();
				if (maxCounter != null && next > maxCounter) {
					break;
				}
				log.debug("get counter (value={}) and init task", next);
				try {
					outputQueue.put(next);
				} catch (InterruptedException e) {
					//FIXME now number will be lost
					log.error("output queue putting error", e);
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

}
