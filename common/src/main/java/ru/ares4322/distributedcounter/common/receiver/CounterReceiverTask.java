package ru.ares4322.distributedcounter.common.receiver;

public interface CounterReceiverTask extends Runnable {

	void setData(byte[] data);

}
