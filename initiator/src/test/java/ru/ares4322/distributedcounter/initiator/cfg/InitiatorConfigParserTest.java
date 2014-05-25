package ru.ares4322.distributedcounter.initiator.cfg;

import org.testng.annotations.Test;
import ru.ares4322.distributedcounter.common.pool.ConnectionPoolConfig;
import ru.ares4322.distributedcounter.common.receiver.ReceiverConfig;
import ru.ares4322.distributedcounter.common.sender.SenderConfig;
import ru.ares4322.distributedcounter.common.sorter.WriterConfig;

import java.nio.file.Path;

import static java.nio.file.Files.createTempFile;
import static org.apache.commons.io.FileUtils.forceDeleteOnExit;
import static org.testng.Assert.assertEquals;
import static ru.ares4322.distributedcounter.initiator.cfg.CliParams.*;

public class InitiatorConfigParserTest {

	@Test
	public void parseOk() throws Exception {
		InitiatorConfigParser parser = new InitiatorConfigParser();

		Path receiverTempFile = createTempFile("receiver_test", ".tmp");
		forceDeleteOnExit(receiverTempFile.toFile());
		Path senderTempFile = createTempFile("sender_test", ".tmp");
		forceDeleteOnExit(senderTempFile.toFile());

		InitiatorConfigParser.Configs expected = new InitiatorConfigParser.Configs(
			new WriterConfig(senderTempFile),
			new WriterConfig(receiverTempFile),
			new ConnectionPoolConfig("127.0.0.1", 1000, 3),
			new ReceiverConfig("127.0.0.2", 2000, 2),
			new SenderConfig(3)
		);

		String[] params = new String[]{
			"-" + LOCAL_SERVER_PORT, expected.getReceiverConfig().getPort() + "",
			"-" + LOCAL_SERVER_ADDRESS, expected.getReceiverConfig().getServerAddress(),
			"-" + REMOTE_SERVER_PORT, expected.getConnectionPoolConfig().getPort() + "",
			"-" + REMOTE_SERVER_ADDRESS, expected.getConnectionPoolConfig().getServerAddress(),
			"-" + RECEIVER_FILE, expected.getReceiverWriterConfig().getFilePath().toAbsolutePath().toString(),
			"-" + SENDER_FILE, expected.getSenderWriterConfig().getFilePath().toAbsolutePath().toString(),
			"-" + SENDER_THREADS, expected.getSenderConfig().getThreads() + "",
			"-" + RECEIVER_THREADS, expected.getReceiverConfig().getThreads() + ""
		};

		InitiatorConfigParser.Configs actual = parser.parse(params);
		assertEquals(actual, expected);
	}

	//TODO add more test cases
}