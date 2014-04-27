package ru.ares4322.distributedcounter.initiator;

import org.testng.annotations.Test;

import java.nio.file.Path;

import static java.nio.file.Files.createTempFile;
import static org.apache.commons.io.FileUtils.forceDeleteOnExit;
import static org.testng.Assert.assertEquals;
import static ru.ares4322.distributedcounter.initiator.InitiatorStartParamsParserImpl.*;

public class InitiatorStartParamsParserImplTest {

	//TODO add InitiatorStartParamsParserImpl injection

	@Test
	public void parseOk() throws Exception {
		InitiatorStartParamsParserImpl parser = new InitiatorStartParamsParserImpl();

		Path receiverTempFile = createTempFile("receiver_test", ".tmp");
		forceDeleteOnExit(receiverTempFile.toFile());
		Path senderTempFile = createTempFile("sender_test", ".tmp");
		forceDeleteOnExit(senderTempFile.toFile());

		InitiatorStartParams expected = new InitiatorStartParams();
		expected.setSenderThreads(2);
		expected.setReceiverThreads(1);
		expected.setServerAddress("127.0.0.1");
		expected.setServerPort(3000);
		expected.setReceiverFilePath(receiverTempFile);
		expected.setSenderFilePath(senderTempFile);

		String[] params = new String[]{
			"-" + SERVER_PORT, expected.getServerPort() + "",
			"-" + SERVER_ADDRESS, expected.getServerAddress(),
			"-" + RECEIVER_FILE, expected.getReceiverFilePath().toAbsolutePath().toString(),
			"-" + SENDER_FILE, expected.getSenderFilePath().toAbsolutePath().toString(),
			"-" + SENDER_THREADS, expected.getSenderThreads() + "",
			"-" + RECEIVER_THREADS, expected.getReceiverThreads() + ""
		};

		InitiatorStartParams actual = parser.parse(params);
		assertEquals(actual, expected);
	}

	//TODO add more test cases
}