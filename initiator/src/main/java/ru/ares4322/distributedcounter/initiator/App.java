package ru.ares4322.distributedcounter.initiator;

import com.google.inject.CreationException;
import com.google.inject.Injector;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.*;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.Stage.PRODUCTION;
import static java.lang.System.exit;
import static org.slf4j.LoggerFactory.getLogger;

public class App {

	private static final Logger log = getLogger(App.class);

	public static void main(String[] args) {

		Injector injector = null;
		try {
			injector = createInjector(
				PRODUCTION,
				new ConfigModule(args),
				new CliModule(),
				new ConnectionPoolModule(),
				new CounterReceiverModule(),
				new CounterSenderModule()
			);

		} catch (CreationException e) {
			if (e.getCause() instanceof StartParamsParserException) {
				log.error("parsing params error:", e);
			} else {
				log.error("context creation error:", e);
			}
			exit(1);
		}
		ConnectionPool pool = injector.getInstance(ConnectionPool.class);
		CounterReceiverService receiverService = injector.getInstance(CounterReceiverService.class);
		CounterSenderService senderService = injector.getInstance(CounterSenderService.class);
		CliCommandReaderService commandReaderService = injector.getInstance(CliCommandReaderService.class);

		receiverService.startAsync().awaitRunning();
		commandReaderService.run();
		pool.init();
		senderService.startAsync().awaitRunning();

	}
}
