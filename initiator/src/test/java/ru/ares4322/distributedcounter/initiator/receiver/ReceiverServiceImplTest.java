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
import ru.ares4322.distributedcounter.common.domain.Packet;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.receiver.ReceiverService;
import ru.ares4322.distributedcounter.common.sender.Sender;
import ru.ares4322.distributedcounter.common.sorter.ReceiverWriter;
import ru.ares4322.distributedcounter.common.sorter.WriterConfig;
import ru.ares4322.distributedcounter.initiator.QueueModule;
import ru.ares4322.distributedcounter.initiator.cfg.ConfigModule;
import ru.ares4322.distributedcounter.initiator.pool.ConnectionPoolModule;
import ru.ares4322.distributedcounter.initiator.sender.SenderModule;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;

import static com.beust.jcommander.internal.Lists.newArrayList;
import static com.google.common.collect.FluentIterable.from;
import static java.lang.Thread.sleep;
import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.readAllLines;
import static java.util.Arrays.asList;
import static java.util.Collections.sort;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.*;
import static ru.ares4322.distributedcounter.initiator.cfg.CliParams.*;

@Guice(
	modules = {
		ConnectionPoolModule.class,
		ReceiverModule.class,
		SenderModule.class,
		QueueModule.class
	},
	moduleFactory = ReceiverServiceImplTest.ConfigModuleFactory.class
)
public class ReceiverServiceImplTest {

	private static final Logger log = getLogger(ReceiverServiceImplTest.class);

	@Inject
	private ReceiverService receiverService;

	@Inject
	private Sender sender;

	@Inject
	@ReceiverWriter
	private WriterConfig config;

	@Inject
	private ConnectionPool pool;

	private final static String CHARSET = "UTF-8";

	private Integer[] numbers = new Integer[]{1, 2, 3, 4, 5};

	private ExecutorService executor = newSingleThreadExecutor();

	@BeforeClass
	public void setUp() throws Exception {
		receiverService.init();
		pool.init();
		executor.execute(receiverService);
	}

	@AfterClass
	public void tearDown() throws Exception {
		pool.close();
		receiverService.shutDown();
		executor.shutdown();
	}

	//FIXME
	@Test(enabled = false)
	public void run() throws Exception {
		for (int number : numbers) {
			sender.send(new Packet(1, number));
		}

		sleep(3000);    //TODO find better way

		assertFileData(config.getFilePath());

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
