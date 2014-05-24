package ru.ares4322.distributedcounter.common.sender;

public interface CounterSenderService extends Runnable {

	void init();

	void startUp();

	void shutDown();

}
