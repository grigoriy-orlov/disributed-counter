package ru.ares4322.distributedcounter.proxy.cfg;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.pool.ConnectionPoolConfig;
import ru.ares4322.distributedcounter.common.receiver.ReceiverConfig;
import ru.ares4322.distributedcounter.common.sender.SenderConfig;
import ru.ares4322.distributedcounter.proxy.Echo;
import ru.ares4322.distributedcounter.proxy.Initiator;

import static org.slf4j.LoggerFactory.getLogger;

public class ConfigModule extends AbstractModule {

	private static final Logger log = getLogger(ConfigModule.class);

	private String[] params;

	public ConfigModule(String[] params) {
		this.params = params;
	}

	@Override
	protected void configure() {
		log.debug("startUp configure ConfigModule");

		ProxyConfigParser.Configs config = new ProxyConfigParser().parse(params);

		binder()
			.bind(ReceiverConfig.class)
			.annotatedWith(Echo.class)
			.toInstance(config.getEchoReceiverConfig());

		binder()
			.bind(ReceiverConfig.class)
			.annotatedWith(Initiator.class)
			.toInstance(config.getInitiatorReceiverConfig());

		binder()
			.bind(ConnectionPoolConfig.class)
			.annotatedWith(Echo.class)
			.toInstance(config.getEchoConnectionPoolConfig());

		binder()
			.bind(ConnectionPoolConfig.class)
			.annotatedWith(Initiator.class)
			.toInstance(config.getInitiatorConnectionPoolConfig());

		binder()
			.bind(SenderConfig.class)
			.annotatedWith(Echo.class)
			.toInstance(config.getEchoSenderConfig());

		binder()
			.bind(SenderConfig.class)
			.annotatedWith(Initiator.class)
			.toInstance(config.getInitiatorSenderConfig());

		log.debug("finish configure ConfigModule");
	}
}
