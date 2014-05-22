package ru.ares4322.distributedcounter.initiator.pool;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;

import static com.google.inject.Scopes.SINGLETON;
import static org.slf4j.LoggerFactory.getLogger;

public class ConnectionPoolModule extends AbstractModule {

	private static final Logger log = getLogger(ConnectionPoolModule.class);

	@Override
	protected void configure() {
		log.debug("start configure ConnectionPoolModule");

		binder()
			.bind(ConnectionPool.class)
			.to(InitiatorConnectionPoolImpl.class)
			.in(SINGLETON);

		log.debug("finish configure ConnectionPoolModule");
	}
}
