package ru.ares4322.distributedcounter.initiator;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.CounterSenderExecutor;
import ru.ares4322.distributedcounter.common.CounterSenderService;
import ru.ares4322.distributedcounter.common.CounterSenderTask;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Provider;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.newBufferedWriter;
import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.slf4j.LoggerFactory.getLogger;

public class CounterSenderServiceImpl extends AbstractExecutionThreadService implements CounterSenderService {

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

	private boolean inProgress;
	private Integer maxCounter;

	@PostConstruct
	@Override
	public void startUp() {
		log.debug("startUp");
		inProgress = true;

		try {
			writer = newBufferedWriter(config.getSenderFilePath(), forName("UTF-8"));
		} catch (IOException e) {
			log.error("counter receiver init selector error", e);
			throw new IllegalStateException("counter receiver starting error");
		}
	}

	@Override
	public void shutDown() {
		log.debug("shutDown");

		inProgress = false;
		if (null != executor) {
			executor.shutdown();
		}
		closeQuietly(writer);
	}

	//TODO beautify
	@Override
	public void run() {
		final int tasks = 3;
		int t = tasks;
		ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<>(executor, new ArrayBlockingQueue<Future<Integer>>(tasks));
		while (inProgress) {
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
				} catch (InterruptedException | ExecutionException e) {
					log.error("task taking error", e);
					//TODO right?
				}
			}
		}
		log.debug("finish counter sending");
	}

	public void setMaxCounter(Integer maxCounter) {
		this.maxCounter = maxCounter;
	}
}
