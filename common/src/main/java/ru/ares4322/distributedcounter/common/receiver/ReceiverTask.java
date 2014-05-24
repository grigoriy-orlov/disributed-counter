package ru.ares4322.distributedcounter.common.receiver;

public interface ReceiverTask extends Runnable {

	void setData(byte[] data);

}
