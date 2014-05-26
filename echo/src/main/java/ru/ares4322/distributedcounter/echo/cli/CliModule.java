package ru.ares4322.distributedcounter.echo.cli;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.cli.Controllable;

import static com.google.inject.Scopes.SINGLETON;
import static org.slf4j.LoggerFactory.getLogger;

public class CliModule extends AbstractModule {

	private static final Logger log = getLogger(CliModule.class);

	@Override
	protected void configure() {
		log.debug("start configure CliModule");

		binder()
			.bind(Controllable.class)
			.to(ControllableImpl.class)
			.in(SINGLETON);

		log.debug("finish configure CliModule");
	}
}
