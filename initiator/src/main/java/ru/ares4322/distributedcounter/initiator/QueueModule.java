package ru.ares4322.distributedcounter.initiator;

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
	@InitiatorToSenderQueue
	public BlockingQueue<Integer> getInitiatorToSenderQueue() {
		return newArrayBlockingQueue(20000);
	}

	@Provides
	@Singleton
	@SenderToSorterQueue
	public BlockingQueue<Integer> getSenderToSorterQueue() {
		return newArrayBlockingQueue(20000);
	}

	@Provides
	@Singleton
	@ReceiverToSorterQueue
	public BlockingQueue<Integer> getReceiverToSorterQueue() {
		return newArrayBlockingQueue(20000);
	}
}
