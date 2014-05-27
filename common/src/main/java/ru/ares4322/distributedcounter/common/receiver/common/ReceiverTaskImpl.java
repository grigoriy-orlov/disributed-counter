package ru.ares4322.distributedcounter.common.receiver.common;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.domain.Packet;
import ru.ares4322.distributedcounter.common.receiver.ReceiverTask;

import javax.inject.Inject;
import java.util.concurrent.BlockingQueue;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.slf4j.LoggerFactory.getLogger;
import static ru.ares4322.distributedcounter.common.util.PacketParser.bytesToPacket;

public class ReceiverTaskImpl implements ReceiverTask {

	private static final Logger log = getLogger(ReceiverServiceImpl.class);

	private final BlockingQueue<Packet> outputQueue;

	private byte[] data;

	@Inject
	public ReceiverTaskImpl(
		BlockingQueue<Packet> outputQueue
	) {
		this.outputQueue = outputQueue;
	}


	@Override
	public void run() {
		checkNotNull(data, "data must be not null");

		log.debug("run");
		Packet packet = bytesToPacket(data);
		log.debug("write num to file (value = {})", packet.getNumber());
		try {
			outputQueue.put(packet);
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
