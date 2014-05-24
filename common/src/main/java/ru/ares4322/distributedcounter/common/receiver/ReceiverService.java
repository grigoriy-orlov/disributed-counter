package ru.ares4322.distributedcounter.common.receiver;

public interface ReceiverService extends Runnable {

	void init();

	void startUp();

	void shutDown();
}
