package ru.ares4322.distributedcounter.echo;

import org.testng.annotations.Test;

import java.nio.file.Path;

import static java.nio.file.Files.createTempFile;
import static org.apache.commons.io.FileUtils.forceDeleteOnExit;
import static org.testng.Assert.assertEquals;
import static ru.ares4322.distributedcounter.echo.EchoConfigParserImpl.*;

public class EchoConfigParserImplTest {

	//TODO add EchoStartParamsParserImpl injection

	@Test
	public void parseOk() throws Exception {
		EchoConfigParser parser = new EchoConfigParserImpl();

		Path tempFile = createTempFile("test", "tmp");
		forceDeleteOnExit(tempFile.toFile());

		EchoConfig expected = new EchoConfig(2, 1, 2000, "127.0.0.1", 3000, "127.0.0.2", tempFile);

		String[] params = new String[]{
			"-" + LOCAL_SERVER_PORT, expected.getLocalServerPort() + "",
			"-" + LOCAL_SERVER_ADDRESS, expected.getLocalServerAddress(),
			"-" + REMOTE_SERVER_PORT, expected.getRemoteServerPort() + "",
			"-" + REMOTE_SERVER_ADDRESS, expected.getRemoteServerAddress(),
			"-" + FILE, expected.getFilePath().toAbsolutePath().toString(),
			"-" + SENDER_THREADS, expected.getSenderThreads() + "",
			"-" + RECEIVER_THREADS, expected.getReceiverThreads() + ""
		};

		EchoConfig actual = parser.parse(params);
		assertEquals(actual, expected);
	}

	//TODO add more test cases
}