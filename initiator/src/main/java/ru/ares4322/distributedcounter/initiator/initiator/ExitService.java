package ru.ares4322.distributedcounter.initiator.initiator;

public interface ExitService extends Runnable {

	void init();

	void startUp();

	void shutDown();
}
