package ru.ares4322.distributedcounter.common.sorter;

public interface WriterTask extends Runnable {

	void setInterval(int from, int length);
}
