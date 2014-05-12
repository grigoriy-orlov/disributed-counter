package ru.ares4322.distributedcounter.initiator;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.Controllable;

import static org.slf4j.LoggerFactory.getLogger;

public class CliModule extends AbstractModule {

	private static final Logger log = getLogger(CliModule.class);

	@Override
	protected void configure() {
		log.debug("start configure CliModule");

		binder().bind(Controllable.class).to(ControllableImpl.class);

		log.debug("finish configure CliModule");
	}
}
