package ru.ares4322.distributedcounter.initiator;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;

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

		InitiatorConfigParser configParser = new InitiatorConfigParserImpl();
		InitiatorConfig config = configParser.parse(params);
		binder().bind(InitiatorConfig.class).toInstance(config);

		log.debug("finish configure ConfigModule");
	}
}
