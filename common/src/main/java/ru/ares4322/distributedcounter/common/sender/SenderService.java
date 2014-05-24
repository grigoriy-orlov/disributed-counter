package ru.ares4322.distributedcounter.common.sender;

public interface SenderService extends Runnable {

	void init();

	void startUp();

	void shutDown();

}
