package ru.ares4322.distributedcounter.common;

import java.io.Writer;

public interface CounterReceiverTask extends Runnable {

	void setData(byte[] data);

	void setWriter(Writer writer);
}
