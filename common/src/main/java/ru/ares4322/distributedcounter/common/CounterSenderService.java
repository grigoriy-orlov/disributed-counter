package ru.ares4322.distributedcounter.common;

import com.google.common.util.concurrent.Service;

public interface CounterSenderService extends Service {

	void setMaxCounter(Integer maxCounter);

	void suspend();

	void resume();
}
