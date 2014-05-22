package ru.ares4322.distributedcounter.common.sender;

public interface CounterSenderTask extends Runnable {

	void setCounter(int counter);

}
