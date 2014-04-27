package ru.ares4322.distributedcounter.proxy;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static ru.ares4322.distributedcounter.proxy.ProxyConfigParserImpl.*;

public class ProxyConfigParserImplTest {
	//TODO add ProxyStartParamsParserImpl injection

	@Test
	public void parseOk() throws Exception {
		ProxyConfigParserImpl parser = new ProxyConfigParserImpl();

		ProxyConfig expected = new ProxyConfig(
			2,1,2000,"127.0.0.1", 3000, "127.0.0.2", 4000, "127.0.0.3", 5000, "127.0.0.4"
		);

		String[] params = new String[]{
			"-" + ECHO_SERVER_PORT, expected.getEchoServerPort() + "",
			"-" + ECHO_SERVER_ADDRESS, expected.getEchoServerAddress(),
			"-" + INITIATOR_SERVER_PORT, expected.getInitiatorServerPort() + "",
			"-" + INITIATOR_SERVER_ADDRESS, expected.getInitiatorServerAddress(),
			"-" + LOCAL_SERVER_PORT, expected.getLocalServerPort() + "",
			"-" + LOCAL_SERVER_ADDRESS, expected.getLocalServerAddress(),
			"-" + OTHER_LOCAL_SERVER_PORT, expected.getOtherLocalServerPort() + "",
			"-" + OTHER_LOCAL_SERVER_ADDRESS, expected.getOtherLocalServerAddress(),
			"-" + SENDER_THREADS, expected.getSenderThreads() + "",
			"-" + RECEIVER_THREADS, expected.getReceiverThreads() + ""
		};

		ProxyConfig actual = parser.parse(params);
		assertEquals(actual, expected);
	}

	//TODO add more test cases

}