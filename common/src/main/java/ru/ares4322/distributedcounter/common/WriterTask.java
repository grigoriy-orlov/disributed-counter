package ru.ares4322.distributedcounter.common;

public interface WriterTask extends Runnable {

	void setInterval(int from, int length);
}
