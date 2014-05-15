package ru.ares4322.distributedcounter.common;

public interface CounterSenderService extends Runnable {

	void setMaxCounter(Integer maxCounter);

	void init();

	void startUp();

	void shutDown();

	void suspend();
}
