package ru.ares4322.distributedcounter.initiator;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.ConnectionPool;

import static org.slf4j.LoggerFactory.getLogger;

public class ConnectionPoolModule extends AbstractModule {

	private static final Logger log = getLogger(ConnectionPoolModule.class);

	@Override
	protected void configure() {
		log.debug("start configure ConnectionPoolModule");

		binder().bind(ConnectionPool.class).to(InitiatorConnectionPoolImpl.class);

		log.debug("finish configure ConnectionPoolModule");
	}
}
