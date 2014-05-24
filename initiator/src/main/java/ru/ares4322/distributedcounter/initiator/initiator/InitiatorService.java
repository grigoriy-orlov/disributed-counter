package ru.ares4322.distributedcounter.initiator.initiator;

public interface InitiatorService extends Runnable{

	void init();

	void startUp();

	void stop();

	void shutDown();

	void setMaxCounter(Integer maxCounter);
}
