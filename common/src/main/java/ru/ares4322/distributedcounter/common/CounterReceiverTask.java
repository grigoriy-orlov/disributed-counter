package ru.ares4322.distributedcounter.common;

public interface CounterReceiverTask extends Runnable {

	void setData(byte[] data);

}
