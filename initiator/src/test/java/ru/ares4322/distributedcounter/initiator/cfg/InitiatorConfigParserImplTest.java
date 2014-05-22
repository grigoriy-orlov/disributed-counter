package ru.ares4322.distributedcounter.initiator.cfg;

import org.testng.annotations.Test;
import java.nio.file.Path;

import static java.nio.file.Files.createTempFile;
import static org.apache.commons.io.FileUtils.forceDeleteOnExit;
import static org.testng.Assert.assertEquals;
import static ru.ares4322.distributedcounter.initiator.cfg.CliParams.*;

public class InitiatorConfigParserImplTest {

	//TODO add InitiatorStartParamsParserImpl injection

	@Test
	public void parseOk() throws Exception {
		InitiatorConfigParserImpl parser = new InitiatorConfigParserImpl();

		Path receiverTempFile = createTempFile("receiver_test", ".tmp");
		forceDeleteOnExit(receiverTempFile.toFile());
		Path senderTempFile = createTempFile("sender_test", ".tmp");
		forceDeleteOnExit(senderTempFile.toFile());

		InitiatorConfig expected = new InitiatorConfig(
			2,1, 1000, "127.0.0.1", 2000, "127.0.0.2", receiverTempFile, senderTempFile
		);

		String[] params = new String[]{
			"--" + LOCAL_SERVER_PORT, expected.getLocalServerPort() + "",
			"--" + LOCAL_SERVER_ADDRESS, expected.getLocalServerAddress(),
			"--" + REMOTE_SERVER_PORT, expected.getRemoteServerPort() + "",
			"--" + REMOTE_SERVER_ADDRESS, expected.getRemoteServerAddress(),
			"--" + RECEIVER_FILE, expected.getReceiverFilePath().toAbsolutePath().toString(),
			"--" + SENDER_FILE, expected.getSenderFilePath().toAbsolutePath().toString(),
			"--" + SENDER_THREADS, expected.getSenderThreads() + "",
			"--" + RECEIVER_THREADS, expected.getReceiverThreads() + ""
		};

		InitiatorConfig actual = parser.parse(params);
		assertEquals(actual, expected);
	}

	//TODO add more test cases
}