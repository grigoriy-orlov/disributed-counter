package ru.ares4322.distributedcounter.initiator.pool;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.pool.common.ConnectionPoolImpl;
import ru.ares4322.distributedcounter.initiator.cfg.InitiatorConfig;

import javax.inject.Singleton;

import static org.slf4j.LoggerFactory.getLogger;

public class ConnectionPoolModule extends AbstractModule {

	private static final Logger log = getLogger(ConnectionPoolModule.class);

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	public ConnectionPool getConnectionPool(InitiatorConfig config) {
		return new ConnectionPoolImpl(config);
	}
}
