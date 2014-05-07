package ru.ares4322.distributedcounter.initiator;

import com.google.common.base.Function;
import com.google.inject.Module;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.testng.IModuleFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import ru.ares4322.distributedcounter.common.ConnectionPool;
import ru.ares4322.distributedcounter.common.CounterReceiverService;
import ru.ares4322.distributedcounter.common.CounterSenderService;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeSet;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Thread.sleep;
import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.readAllLines;
import static java.util.Collections.sort;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.*;
import static ru.ares4322.distributedcounter.initiator.InitiatorConfigParserImpl.*;

//TODO move to integration test
@Guice(
	modules = {
		ConnectionPoolModule.class,
		CounterReceiverModule.class,
		CounterSenderModule.class
	},
	moduleFactory = CounterSenderReceiverTest.ConfigModuleFactory.class
)
public class CounterSenderReceiverTest {

	private static final Logger log = getLogger(CounterSenderReceiverTest.class);

	@Inject
	private CounterReceiverService receiverService;
	@Inject
	private CounterSenderService senderService;

	@Inject
	private InitiatorConfig config;

	@Inject
	private ConnectionPool pool;

	private final static String CHARSET = "UTF-8";


	@BeforeClass
	public void setUp() throws Exception {
		senderService.setMaxCounter(100);
	}

	@Test
	public void test() throws Exception {
		receiverService.startAsync().awaitRunning();
		pool.init();
		senderService.startAsync().awaitRunning();

		sleep(2000);

		senderService.stopAsync();
		receiverService.stopAsync();

		assertFileData(config.getReceiverFilePath(), config.getSenderFilePath());
	}

	@AfterMethod
	public void tearDown() throws Exception {
		pool.close();
	}

	private void assertFileData(Path receiverFilePath, Path senderFilePath) throws IOException {
		List<String> receiverLines = readAllLines(receiverFilePath, forName(CHARSET));
		assertNotNull(receiverLines);
		assertFalse(receiverLines.isEmpty());

		List<String> senderLines = readAllLines(senderFilePath, forName(CHARSET));
		assertNotNull(senderLines);
		assertFalse(senderLines.isEmpty());

		assertEquals(
			new TreeSet<>(receiverLines),
			new TreeSet<>(senderLines),
			"sender and receiver number sequences must have the same elements"
		);

		List<Integer> receiverIntegers = newArrayList(from(receiverLines)
			.transform(new Function<String, Integer>() {
				@Override
				public Integer apply(String input) {
					return Integer.valueOf(input);
				}
			})
			.toList());
		sort(receiverIntegers);

		int i = receiverIntegers.get(0);
		for (Integer integer : receiverIntegers) {
			assertEquals((int) integer, i++, "number sequence is not sequential");
		}

	}

	public static class ConfigModuleFactory implements IModuleFactory {

		@Override
		public Module createModule(ITestContext context, Class<?> testClass) {
			int port = RandomUtils.nextInt(20000, 40000);
			return new ConfigModule(
				new String[]{
					"-" + LOCAL_SERVER_PORT, port + "",
					"-" + LOCAL_SERVER_ADDRESS, "127.0.0.1",
					"-" + REMOTE_SERVER_PORT, port + "",
					"-" + REMOTE_SERVER_ADDRESS, "127.0.0.1",
					"-" + SENDER_THREADS, "3",
					"-" + RECEIVER_THREADS, "3"
				}
			);
		}
	}
}
