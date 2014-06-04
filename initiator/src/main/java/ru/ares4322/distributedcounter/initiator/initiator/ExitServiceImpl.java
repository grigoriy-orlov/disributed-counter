package ru.ares4322.distributedcounter.initiator.initiator;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.domain.Packet;
import ru.ares4322.distributedcounter.initiator.Exit;

import javax.inject.Inject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.LoggerFactory.getLogger;

class ExitServiceImpl implements ExitService {

	private static final Logger log = getLogger(InitiatorServiceImpl.class);

	private final BlockingQueue<Packet> inputQueue;
	private final BlockingQueue<Packet> outputQueue;
	private final CountDownLatch exitLatch;
	//TODO move to module
	private final ExecutorService serviceExecutor = newSingleThreadExecutor(
		new BasicThreadFactory.Builder().namingPattern("ExitService-%s").build()
	);

	private volatile boolean inWork;

	@Inject
	public ExitServiceImpl(
		BlockingQueue<Packet> inputQueue,
		BlockingQueue<Packet> outputQueue,
		@Exit CountDownLatch exitLatch
	) {
		this.inputQueue = inputQueue;
		this.outputQueue = outputQueue;
		this.exitLatch = exitLatch;
	}

	@Override
	public void init() {

	}

	@Override
	public void startUp() {
		inWork = true;
		serviceExecutor.execute(this);
	}

	@Override
	public void shutDown() {
		inWork = false;
		serviceExecutor.shutdown();
	}

	@Override
	public void run() {
		while (inWork) {
			try {
				Packet packet = inputQueue.poll(1, SECONDS);
				if (packet == null) {
					continue;
				}
				if (packet.getState() == 4) {
					exitLatch.countDown();
					return;
				}
				outputQueue.put(packet);
			} catch (InterruptedException e) {
				log.error("input queue taking error", e);
				break;
			}
		}
	}
}
