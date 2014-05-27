package ru.ares4322.distributedcounter.common.sender.common;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.domain.Packet;
import ru.ares4322.distributedcounter.common.sender.Sender;
import ru.ares4322.distributedcounter.common.sender.SenderTask;

import javax.inject.Inject;

import static org.slf4j.LoggerFactory.getLogger;

public class SenderTaskImpl implements SenderTask {

	private static final Logger log = getLogger(SenderTaskImpl.class);

	private final Sender sender;

	private Packet packet;

	@Inject
	public SenderTaskImpl(
		Sender sender
	) {
		this.sender = sender;
	}

	@Override
	public void run() {
		log.debug("send data and write to file");
		sender.send(packet);
	}

	@Override
	public void setPacket(Packet packet) {
		this.packet = packet;
	}

}
