package ru.ares4322.distributedcounter.common;

import java.io.BufferedWriter;

public interface CounterSenderTask extends Runnable {

	void setCounter(int counter);

	void setWriter(BufferedWriter writer);
}
