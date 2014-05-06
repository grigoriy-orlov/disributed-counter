package ru.ares4322.distributedcounter.common;

import java.io.FileOutputStream;

public interface CounterReceiverTask extends Runnable {

	void setData(byte[] data);

	//TODO stream->writer
	void setStream(FileOutputStream stream);
}
