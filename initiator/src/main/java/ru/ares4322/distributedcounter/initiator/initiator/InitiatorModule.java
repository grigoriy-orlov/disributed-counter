package ru.ares4322.distributedcounter.initiator.initiator;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ru.ares4322.distributedcounter.common.domain.Packet;
import ru.ares4322.distributedcounter.initiator.Exit;
import ru.ares4322.distributedcounter.initiator.ExitorToSorterQueue;
import ru.ares4322.distributedcounter.initiator.InitiatorToSenderQueue;
import ru.ares4322.distributedcounter.initiator.ReceiverToExitorQueue;

import javax.inject.Singleton;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class InitiatorModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	public InitiatorService getInitiatorService(
		@InitiatorToSenderQueue BlockingQueue<Packet> outputQueue
	) {
		return new InitiatorServiceImpl(outputQueue);
	}

	@Provides
	@Singleton
	@Exit
	public CountDownLatch getExitLatch() {
		return new CountDownLatch(1);
	}

	@Provides
	@Singleton
	public ExitService getExitService(
		@ReceiverToExitorQueue BlockingQueue<Packet> inputQueue,
		@ExitorToSorterQueue BlockingQueue<Packet> outputQueue,
		@Exit CountDownLatch exitLatch
	) {
		return new ExitServiceImpl(inputQueue, outputQueue, exitLatch);
	}
}
