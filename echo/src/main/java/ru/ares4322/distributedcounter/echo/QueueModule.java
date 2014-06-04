package ru.ares4322.distributedcounter.echo;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ru.ares4322.distributedcounter.common.domain.Packet;

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
	public BlockingQueue<Packet> getReceiverToSenderQueue() {
		return newArrayBlockingQueue(20000);
	}

	@Provides
	@Singleton
	@SenderToSorterQueue
	public BlockingQueue<Packet> getReceiverSorterQueue() {
		return newArrayBlockingQueue(20000);
	}

	@Provides
	@Singleton
	@BlackHoleQueue
	public BlockingQueue<Packet> getSenderSorterQueue() {
		return new ru.ares4322.distributedcounter.common.util.BlackHoleQueue<>();
	}

}
