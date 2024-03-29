package ru.ares4322.distributedcounter.initiator.sender;

import com.google.common.base.Function;
import com.google.inject.Module;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.testng.IModuleFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import ru.ares4322.distributedcounter.common.cli.Controllable;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.sorter.ReceiverWriter;
import ru.ares4322.distributedcounter.common.sorter.SenderWriter;
import ru.ares4322.distributedcounter.common.sorter.SorterService;
import ru.ares4322.distributedcounter.common.sorter.WriterConfig;
import ru.ares4322.distributedcounter.initiator.QueueModule;
import ru.ares4322.distributedcounter.initiator.ReceiverSorter;
import ru.ares4322.distributedcounter.initiator.cfg.ConfigModule;
import ru.ares4322.distributedcounter.initiator.cli.CliModule;
import ru.ares4322.distributedcounter.initiator.initiator.InitiatorModule;
import ru.ares4322.distributedcounter.initiator.pool.ConnectionPoolModule;
import ru.ares4322.distributedcounter.initiator.receiver.ReceiverModule;
import ru.ares4322.distributedcounter.initiator.sorter.ReceiverSorterModule;
import ru.ares4322.distributedcounter.initiator.sorter.SenderSorterModule;

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
import static ru.ares4322.distributedcounter.initiator.cfg.CliParams.*;

//TODO move to integration test
@Guice(
	modules = {
		CliModule.class,
		ConnectionPoolModule.class,
		ReceiverModule.class,
		ReceiverSorterModule.class,
		QueueModule.class,
		SenderModule.class,
		SenderSorterModule.class,
		InitiatorModule.class
	},
	moduleFactory = SenderReceiverTest.ConfigModuleFactory.class
)
public class SenderReceiverTest {

	private static final Logger log = getLogger(SenderReceiverTest.class);

	@Inject
	@ReceiverSorter
	private SorterService sorterService;

	@Inject
	private Controllable controllable;

	@Inject
	@ReceiverWriter
	private WriterConfig receiverWriterConfig;

	@Inject
	@SenderWriter
	private WriterConfig senderWriterConfig;

	@Inject
	private ConnectionPool pool;

	private final static String CHARSET = "UTF-8";

	//FIXME
	@Test(enabled = false)
	public void test() throws Exception {
		controllable.init();

		//senderService.setMaxCounter(100);

		pool.init();
		sorterService.startUp();
		controllable.start();

		sleep(100);

		controllable.stop();

		//senderService.setMaxCounter(200);

		sleep(2000);

		assertFileData(receiverWriterConfig.getFilePath(), senderWriterConfig.getFilePath());

		sleep(2000);

		controllable.start();

		sleep(100);

		controllable.stop();

		assertFileData(receiverWriterConfig.getFilePath(), senderWriterConfig.getFilePath());
	}

	@AfterMethod
	public void tearDown() throws Exception {
		controllable.exit();
		pool.close();
		sorterService.shutDown();
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
