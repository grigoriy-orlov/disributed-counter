package ru.ares4322.distributedcounter.echo.cfg;

import org.testng.annotations.Test;
import ru.ares4322.distributedcounter.common.pool.ConnectionPoolConfig;
import ru.ares4322.distributedcounter.common.receiver.ReceiverConfig;
import ru.ares4322.distributedcounter.common.sender.SenderConfig;
import ru.ares4322.distributedcounter.common.sorter.WriterConfig;

import java.nio.file.Path;

import static java.nio.file.Files.createTempFile;
import static org.apache.commons.io.FileUtils.forceDeleteOnExit;
import static org.testng.Assert.assertEquals;
import static ru.ares4322.distributedcounter.echo.cfg.CliParams.*;

public class EchoConfigParserTest {

	@Test
	public void parseOk() throws Exception {
		EchoConfigParser parser = new EchoConfigParser();

		Path tempFile = createTempFile("test", "tmp");
		forceDeleteOnExit(tempFile.toFile());

		EchoConfigParser.Configs expected = new EchoConfigParser.Configs(
			new WriterConfig(tempFile),
			new ConnectionPoolConfig("127.0.0.1", 2000, 3),
			new ReceiverConfig("localhost", 3000, 2),
			new SenderConfig(3)
		);

		String[] params = new String[]{
			"-" + LOCAL_SERVER_PORT, expected.getReceiverConfig().getPort() + "",
			"-" + LOCAL_SERVER_ADDRESS, expected.getReceiverConfig().getServerAddress(),
			"-" + REMOTE_SERVER_PORT, expected.getConnectionPoolConfig().getPort() + "",
			"-" + REMOTE_SERVER_ADDRESS, expected.getConnectionPoolConfig().getServerAddress(),
			"-" + FILE, expected.getWriterConfig().getFilePath().toAbsolutePath().toString(),
			"-" + SENDER_THREADS, expected.getSenderConfig().getThreads() + "",
			"-" + RECEIVER_THREADS, expected.getReceiverConfig().getThreads() + ""
		};

		EchoConfigParser.Configs actual = parser.parse(params);
		assertEquals(actual, expected);
	}

	//TODO add more test cases
}