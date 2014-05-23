package ru.ares4322.distributedcounter.initiator.receiver;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.receiver.CounterReceiverQueue;
import ru.ares4322.distributedcounter.common.receiver.CounterReceiverTask;

import javax.inject.Inject;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.slf4j.LoggerFactory.getLogger;
import static ru.ares4322.distributedcounter.common.util.Utils.networkByteArrayToInt;

class CounterReceiverTaskImpl implements CounterReceiverTask {

	private static final Logger log = getLogger(CounterReceiverServiceImpl.class);

	private final Queue<Integer> queue;

	private byte[] data;

	@Inject
	public CounterReceiverTaskImpl(
		@CounterReceiverQueue Queue<Integer> queue
	) {
		this.queue = queue;
	}


	@Override
	public void run() {
		checkNotNull(data, "data must be not null");

		log.debug("run");
		int num = networkByteArrayToInt(data);
		log.debug("write num to file (value = {})", num);
		queue.add(num);
	}

	@Override
	public void setData(byte[] data) {
		this.data = data;
	}

}
