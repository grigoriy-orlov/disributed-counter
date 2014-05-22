package ru.ares4322.distributedcounter.initiator;

import com.google.inject.CreationException;
import com.google.inject.Injector;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.cfg.StartParamsParserException;
import ru.ares4322.distributedcounter.common.cli.CliCommandReaderService;
import ru.ares4322.distributedcounter.common.cli.Controllable;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.receiver.CounterReceiverService;
import ru.ares4322.distributedcounter.common.sender.CounterSenderService;
import ru.ares4322.distributedcounter.common.sorter.SorterService;
import ru.ares4322.distributedcounter.initiator.cfg.ConfigModule;
import ru.ares4322.distributedcounter.initiator.cli.CliModule;
import ru.ares4322.distributedcounter.initiator.pool.ConnectionPoolModule;
import ru.ares4322.distributedcounter.initiator.receiver.CounterReceiverModule;
import ru.ares4322.distributedcounter.initiator.sender.CounterSenderModule;
import ru.ares4322.distributedcounter.initiator.sorter.SorterModule;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.Stage.PRODUCTION;
import static java.lang.System.exit;
import static java.lang.Thread.sleep;
import static org.slf4j.LoggerFactory.getLogger;

public class App {

	private static final Logger log = getLogger(App.class);

	public static void main(String[] args) throws InterruptedException {

		Injector injector = null;
		try {
			injector = createInjector(
				PRODUCTION,
				new ConfigModule(args),
				new CliModule(),
				new ConnectionPoolModule(),
				new SorterModule(),
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
		SorterService sorterService = injector.getInstance(SorterService.class);
		Controllable controllable = injector.getInstance(Controllable.class);

		receiverService.startAsync().awaitRunning();
		pool.init();
		senderService.init();
		controllable.init();
		sorterService.start();
		commandReaderService.run();
		//FIXME
		sleep(2000);
		exit(0);
	}
}
