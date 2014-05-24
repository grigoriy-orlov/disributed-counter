package ru.ares4322.distributedcounter.common.sender;

public interface SenderTask extends Runnable {

	void setCounter(int counter);

}
