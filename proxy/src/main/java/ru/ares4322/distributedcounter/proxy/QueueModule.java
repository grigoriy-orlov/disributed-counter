package ru.ares4322.distributedcounter.proxy;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;
import java.util.concurrent.BlockingQueue;

import static com.google.common.collect.Queues.newArrayBlockingQueue;

public class QueueModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	@InitiatorReceiverToEchoSenderQueue
	public BlockingQueue<Integer> getInitiatorToSenderQueue() {
		return newArrayBlockingQueue(20000);
	}

	@Provides
	@Singleton
	@EchoReceiverToInitiatorSenderQueue
	public BlockingQueue<Integer> getSenderToSorterQueue() {
		return newArrayBlockingQueue(20000);
	}

	@Provides
	@Singleton
	@BlackHoleQueue
	public BlockingQueue<Integer> getBlackHoleQueue() {
		return new ru.ares4322.distributedcounter.common.util.BlackHoleQueue<>();
	}

}
