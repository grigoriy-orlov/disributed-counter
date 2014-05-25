package ru.ares4322.distributedcounter.initiator.cfg;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.pool.ConnectionPoolConfig;
import ru.ares4322.distributedcounter.common.receiver.ReceiverConfig;
import ru.ares4322.distributedcounter.common.sender.SenderConfig;
import ru.ares4322.distributedcounter.common.sorter.ReceiverWriter;
import ru.ares4322.distributedcounter.common.sorter.SenderWriter;
import ru.ares4322.distributedcounter.common.sorter.WriterConfig;

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

		InitiatorConfigParser.Configs config = new InitiatorConfigParser().parse(params);

		binder()
			.bind(ReceiverConfig.class)
			.toInstance(config.getReceiverConfig());

		binder()
			.bind(ConnectionPoolConfig.class)
			.toInstance(config.getConnectionPoolConfig());

		binder()
			.bind(WriterConfig.class)
			.annotatedWith(ReceiverWriter.class)
			.toInstance(config.getReceiverWriterConfig());

		binder()
			.bind(WriterConfig.class)
			.annotatedWith(SenderWriter.class)
			.toInstance(config.getSenderWriterConfig());

		binder()
			.bind(SenderConfig.class)
			.toInstance(config.getSenderConfig());

		log.debug("finish configure ConfigModule");
	}
}
