package ru.ares4322.distributedcounter.initiator.receiver;

import com.google.common.base.Function;
import com.google.inject.Module;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.testng.IModuleFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.receiver.CounterReceiverService;
import ru.ares4322.distributedcounter.common.sender.CounterSender;
import ru.ares4322.distributedcounter.initiator.cfg.ConfigModule;
import ru.ares4322.distributedcounter.initiator.cfg.InitiatorConfig;
import ru.ares4322.distributedcounter.initiator.pool.ConnectionPoolModule;
import ru.ares4322.distributedcounter.initiator.sender.CounterSenderModule;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeSet;

import static com.beust.jcommander.internal.Lists.newArrayList;
import static com.google.common.collect.FluentIterable.from;
import static java.lang.Thread.sleep;
import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.readAllLines;
import static java.util.Arrays.asList;
import static java.util.Collections.sort;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.*;
import static ru.ares4322.distributedcounter.initiator.cfg.CliParams.*;

@Guice(
	modules = {
		ConnectionPoolModule.class,
		CounterReceiverModule.class,
		CounterSenderModule.class
	},
	moduleFactory = CounterReceiverServiceImplTest.ConfigModuleFactory.class
)
public class CounterReceiverServiceImplTest {

	private static final Logger log = getLogger(CounterReceiverServiceImplTest.class);

	@Inject
	private CounterReceiverService receiverService;

	@Inject
	private CounterSender counterSender;

	@Inject
	private InitiatorConfig config;

	@Inject
	private ConnectionPool pool;

	private final static String CHARSET = "UTF-8";

	private Integer[] numbers = new Integer[]{1, 2, 3, 4, 5};

	@BeforeClass
	public void setUp() throws Exception {
		receiverService.startAsync().awaitRunning();

		pool.init();
	}

	@AfterClass
	public void tearDown() throws Exception {
		pool.close();
	}

	//FIXME
	@Test(enabled = false)
	public void run() throws Exception {
		for (int number : numbers) {
			counterSender.send(number);
		}

		sleep(3000);    //TODO find better way

		assertFileData(config.getReceiverFilePath());

		receiverService.stopAsync();    //TODO need awaitTerminated?
	}

	//TODO beautify
	private void assertFileData(Path receiverFilePath) throws IOException {
		List<String> receiverLines = readAllLines(receiverFilePath, forName(CHARSET));
		assertNotNull(receiverLines);
		assertFalse(receiverLines.isEmpty());

		assertEquals(
			new TreeSet<>(receiverLines),
			new TreeSet<>(from(asList(numbers)).transform(new Function<Integer, String>() {
				public String apply(Integer input) {
					return input.toString();
				}
			}).toSet()),
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
