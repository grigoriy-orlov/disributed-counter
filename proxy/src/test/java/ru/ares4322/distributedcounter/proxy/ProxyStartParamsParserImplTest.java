package ru.ares4322.distributedcounter.proxy;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static ru.ares4322.distributedcounter.proxy.ProxyStartParamsParserImpl.*;

public class ProxyStartParamsParserImplTest {
	//TODO add ProxyStartParamsParserImpl injection

	@Test
	public void parseOk() throws Exception {
		ProxyStartParamsParserImpl parser = new ProxyStartParamsParserImpl();

		ProxyStartParams expected = new ProxyStartParams();
		expected.setSenderThreads(2);
		expected.setReceiverThreads(1);
		expected.setEchoServerAddress("127.0.0.1");
		expected.setEchoServerPort(3000);
		expected.setInitiatorServerAddress("localhost");
		expected.setInitiatorServerPort(4000);

		String[] params = new String[]{
			"-" + ECHO_SERVER_PORT, expected.getEchoServerPort() + "",
			"-" + ECHO_SERVER_ADDRESS, expected.getEchoServerAddress(),
			"-" + INITIATOR_SERVER_PORT, expected.getInitiatorServerPort() + "",
			"-" + INITIATOR_SERVER_ADDRESS, expected.getInitiatorServerAddress(),
			"-" + SENDER_THREADS, expected.getSenderThreads() + "",
			"-" + RECEIVER_THREADS, expected.getReceiverThreads() + ""
		};

		ProxyStartParams actual = parser.parse(params);
		assertEquals(actual, expected);
	}

	//TODO add more test cases

}