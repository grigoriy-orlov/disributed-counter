package ru.ares4322.distributedcounter.proxy.pool;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.pool.ConnectionPoolConfig;
import ru.ares4322.distributedcounter.common.pool.common.ConnectionPoolImpl;
import ru.ares4322.distributedcounter.proxy.Echo;
import ru.ares4322.distributedcounter.proxy.Initiator;

import javax.inject.Singleton;

import static org.slf4j.LoggerFactory.getLogger;

public class ConnectionPoolModule extends AbstractModule {

	private static final Logger log = getLogger(ConnectionPoolModule.class);

	@Override
	protected void configure() {
	}

	@Provides
	@Echo
	@Singleton
	public ConnectionPool getEchoConnectionPool(@Echo ConnectionPoolConfig config) {
		return new ConnectionPoolImpl(config);
	}

	@Provides
	@Initiator
	@Singleton
	public ConnectionPool getInitiatorConnectionPool(@Initiator ConnectionPoolConfig config) {
		return new ConnectionPoolImpl(config);
	}
}
