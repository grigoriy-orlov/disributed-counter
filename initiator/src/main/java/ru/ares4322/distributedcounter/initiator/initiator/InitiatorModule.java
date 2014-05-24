package ru.ares4322.distributedcounter.initiator.initiator;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ru.ares4322.distributedcounter.initiator.InitiatorToSenderQueue;

import javax.inject.Singleton;
import java.util.concurrent.BlockingQueue;

public class InitiatorModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	public InitiatorService getInitiatorService(
		@InitiatorToSenderQueue BlockingQueue<Integer> outputQueue
	) {
		return new InitiatorServiceImpl(outputQueue);
	}
}
