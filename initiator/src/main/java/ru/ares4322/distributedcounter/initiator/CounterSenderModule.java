package ru.ares4322.distributedcounter.initiator;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.CounterSender;

import static org.slf4j.LoggerFactory.getLogger;

public class CounterSenderModule extends AbstractModule {

	private static final Logger log = getLogger(CounterSenderModule.class);

	@Override
	protected void configure() {
		log.debug("start configure CounterSenderModule");

		binder().bind(CounterSender.class).to(CounterSenderImpl.class);

		log.debug("finish configure CounterSenderModule");
	}

}
