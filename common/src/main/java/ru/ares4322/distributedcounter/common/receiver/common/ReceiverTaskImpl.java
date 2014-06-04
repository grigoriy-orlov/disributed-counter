package ru.ares4322.distributedcounter.common.receiver.common;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.domain.Packet;
import ru.ares4322.distributedcounter.common.receiver.ReceiverTask;

import javax.inject.Inject;
import java.util.concurrent.BlockingQueue;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.util.concurrent.Uninterruptibles.putUninterruptibly;
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
		logState(packet);
		int state = packet.getState();
		if (state == 2 || state == 4) {
			putUninterruptibly(outputQueue, packet);
		}
	}

	@Override
	public void setData(byte[] data) {
		this.data = data;
	}

	private void logState(Packet packet) {
		switch (packet.getState()) {
			case 0:
				log.info("initiator init");
				break;
			case 1:
				log.info("initiator start");
				break;
			case 2:
				break;
			case 3:
				log.info("initiator stop");
				break;
			case 4:
				log.info("initiator exit");
				break;
			default:
				throw new IllegalStateException("unknown state: " + packet.getState());
		}
	}
}
