package ru.ares4322.distributedcounter.common.receiver;

public interface CounterReceiverService extends Runnable {

	void init();

	void startUp();

	void shutDown();
}
