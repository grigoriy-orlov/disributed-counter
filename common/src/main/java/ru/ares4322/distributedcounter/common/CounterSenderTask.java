package ru.ares4322.distributedcounter.common;

public interface CounterSenderTask extends Runnable {

	void setCounter(int counter);

}
