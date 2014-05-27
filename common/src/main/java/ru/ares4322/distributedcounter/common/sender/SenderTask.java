package ru.ares4322.distributedcounter.common.sender;

import ru.ares4322.distributedcounter.common.domain.Packet;

public interface SenderTask extends Runnable {

	void setPacket(Packet packet);

}
