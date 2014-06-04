package ru.ares4322.distributedcounter.common.sender.common;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.domain.Packet;
import ru.ares4322.distributedcounter.common.sender.SenderService;
import ru.ares4322.distributedcounter.common.sender.SenderTask;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.*;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.slf4j.LoggerFactory.getLogger;

public class SenderServiceImpl implements SenderService {

	private static final Logger log = getLogger(SenderServiceImpl.class);

	private final Provider<SenderTask> counterSenderTaskProvider;
	private final ExecutorService taskExecutor;
	private final BlockingQueue<Packet> inputQueue;
	private final BlockingQueue<Packet> outputQueue;
	//TODO move to module
	private final ExecutorService serviceExecutor = newSingleThreadExecutor(
		new BasicThreadFactory.Builder().namingPattern("CounterSenderService-%s").daemon(true).build()
	);

	private boolean inWork;

	@Inject
	public SenderServiceImpl(
		Provider<SenderTask> counterSenderTaskProvider,
		ExecutorService taskExecutor,
		BlockingQueue<Packet> inputQueue,
		BlockingQueue<Packet> outputQueue
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
			try {
				Packet next = inputQueue.take();
				if (!inWork) {
					break;
				}
				log.debug("get counter (value={}) and init task", next);
				SenderTask task = counterSenderTaskProvider.get();
				task.setPacket(next);
				completionService.submit(task, 1);
				if (next.getState() == 2) {
					outputQueue.put(next);
				}
				t--;
				if (t == 0) {
					try {
						completionService.take().get();
						t++;
					} catch (ExecutionException e) {
						log.error("task taking from future error", e);
						t++;
					}
				}
			} catch (InterruptedException e) {
				log.error("queue taking or putting error, exit", e);
				break;
			}
		}

	}
}
