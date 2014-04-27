package ru.ares4322.distributedcounter.echo;

import org.testng.annotations.Test;

import java.nio.file.Path;

import static java.nio.file.Files.createTempFile;
import static org.apache.commons.io.FileUtils.forceDeleteOnExit;
import static org.testng.Assert.assertEquals;
import static ru.ares4322.distributedcounter.echo.EchoStartParamsParserImpl.*;

public class EchoStartParamsParserImplTest {

	//TODO add EchoStartParamsParserImpl injection

	@Test
	public void parseOk() throws Exception {
		EchoStartParamsParser parser = new EchoStartParamsParserImpl();

		Path tempFile = createTempFile("test", "tmp");
		forceDeleteOnExit(tempFile.toFile());

		EchoStartParams expected = new EchoStartParams();
		expected.setSenderThreads(2);
		expected.setReceiverThreads(1);
		expected.setServerAddress("127.0.0.1");
		expected.setServerPort(3000);
		expected.setFilePath(tempFile);

		String[] params = new String[]{
			"-" + SERVER_PORT, expected.getServerPort() + "",
			"-" + SERVER_ADDRESS, expected.getServerAddress(),
			"-" + FILE, expected.getFilePath().toAbsolutePath().toString(),
			"-" + SENDER_THREADS, expected.getSenderThreads() + "",
			"-" + RECEIVER_THREADS, expected.getReceiverThreads() + ""
		};

		EchoStartParams actual = parser.parse(params);
		assertEquals(actual, expected);
	}

	//TODO add more test cases
}