package ru.ares4322.distributedcounter.initiator.initiator;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.domain.Packet;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.util.concurrent.Uninterruptibles.putUninterruptibly;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.slf4j.LoggerFactory.getLogger;

class InitiatorServiceImpl implements InitiatorService {

	private static final Logger log = getLogger(InitiatorServiceImpl.class);

	private final BlockingQueue<Packet> outputQueue;
	//TODO move to module
	private final ExecutorService serviceExecutor = newSingleThreadExecutor(
		new BasicThreadFactory.Builder().namingPattern("InitiatorService-%s").daemon(true).build()
	);

	//TODO do just Integer
	private AtomicInteger counter = new AtomicInteger();
	private Integer maxCounter;
	private ReentrantLock lock = new ReentrantLock(true);
	//TODO imt -> Enum
	private volatile int state; // 0 - init, 1 - start, 2 - run, 3 - stop, 4 - exit
	private volatile boolean inWork;

	public InitiatorServiceImpl(BlockingQueue<Packet> outputQueue) {
		this.outputQueue = outputQueue;
	}

	@Override
	public void init() {
		log.debug("init");
		putUninterruptibly(outputQueue, new Packet(state, -1));
		if (!lock.isLocked()) {
			//FIXME not atomicity
			lock.lock();
		}
	}

	@Override
	public void startUp() {
		log.debug("startUp");
		inWork = true;
		state = 1;
		putUninterruptibly(outputQueue, new Packet(state, -1));
		state = 2;
		if (lock.isLocked()) {
			//FIXME not atomicity
			lock.unlock();
		}
		serviceExecutor.execute(this);
	}

	@Override
	public void stop() {
		log.debug("start suspend");
		lock.lock();
		state = 3;
		putUninterruptibly(outputQueue, new Packet(state, -1));
		log.debug("finish suspend");
	}

	@Override
	public void shutDown() {
		log.debug("shutDown");
		lock.lock();
		state = 4;
		if (null != serviceExecutor) {
			serviceExecutor.shutdownNow();
		}
		putUninterruptibly(outputQueue, new Packet(state, -1));
		inWork = false;
		lock.unlock();
		log.debug("shutDown complete");
	}


	@Override
	public void run() {
		log.debug("run");
		while (inWork) {
			lock.lock();
			try {
				int next = counter.getAndIncrement();
				if (maxCounter != null && next > maxCounter) {
					break;
				}
				log.debug("get counter (value={}) and init task", next);
				try {
					outputQueue.put(new Packet(state, next));
				} catch (InterruptedException e) {
					log.debug("output queue putting error, finish", e);
					return;
				}
			} finally {
				lock.unlock();
			}
		}
	}

	@Override
	public void setMaxCounter(Integer maxCounter) {
		this.maxCounter = maxCounter;
	}

}
