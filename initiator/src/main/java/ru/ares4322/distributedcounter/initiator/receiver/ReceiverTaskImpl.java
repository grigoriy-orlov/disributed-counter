package ru.ares4322.distributedcounter.initiator.receiver;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.receiver.ReceiverTask;

import javax.inject.Inject;
import java.util.concurrent.BlockingQueue;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.slf4j.LoggerFactory.getLogger;
import static ru.ares4322.distributedcounter.common.util.Utils.networkByteArrayToInt;

class ReceiverTaskImpl implements ReceiverTask {

	private static final Logger log = getLogger(ReceiverServiceImpl.class);

	private final BlockingQueue<Integer> outputQueue;

	private byte[] data;

	@Inject
	public ReceiverTaskImpl(
		BlockingQueue<Integer> outputQueue
	) {
		this.outputQueue = outputQueue;
	}


	@Override
	public void run() {
		checkNotNull(data, "data must be not null");

		log.debug("run");
		int num = networkByteArrayToInt(data);
		log.debug("write num to file (value = {})", num);
		try {
			outputQueue.put(num);
		//FIXME now number will be lost
		} catch (InterruptedException e) {
			log.error("output queue putting error", e);
		}
	}

	@Override
	public void setData(byte[] data) {
		this.data = data;
	}

}
