package ru.ares4322.distributedcounter.echo;

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
	@ReceiverToSenderQueue
	public BlockingQueue<Integer> getReceiverToSenderQueue() {
		return newArrayBlockingQueue(20000);
	}

	@Provides
	@Singleton
	@ReceiverToSorterQueue
	public BlockingQueue<Integer> getReceiverSorterQueue() {
		return newArrayBlockingQueue(20000);
	}

	@Provides
	@Singleton
	@BlackHoleQueue
	public BlockingQueue<Integer> getSenderSorterQueue() {
		return new ru.ares4322.distributedcounter.common.util.BlackHoleQueue<>();
	}

}
