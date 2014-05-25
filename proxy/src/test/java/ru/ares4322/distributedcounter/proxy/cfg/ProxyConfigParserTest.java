package ru.ares4322.distributedcounter.proxy.cfg;

import org.testng.annotations.Test;
import ru.ares4322.distributedcounter.common.pool.ConnectionPoolConfig;
import ru.ares4322.distributedcounter.common.receiver.ReceiverConfig;
import ru.ares4322.distributedcounter.common.sender.SenderConfig;

import static org.testng.Assert.assertEquals;
import static ru.ares4322.distributedcounter.proxy.cfg.CliParams.*;
import static ru.ares4322.distributedcounter.proxy.cfg.ProxyConfigParser.Configs;

public class ProxyConfigParserTest {
	//TODO add ProxyStartParamsParserImpl injection

	@Test
	public void parseOk() throws Exception {
		ProxyConfigParser parser = new ProxyConfigParser();

		Configs expected = new Configs(
			new ConnectionPoolConfig("127.0.0.1", 2000, 2),
			new ConnectionPoolConfig("127.0.0.2", 3000, 2),
			new ReceiverConfig("127.0.0.3", 4000, 3),
			new ReceiverConfig("127.0.0.4", 5000, 3),
			new SenderConfig(2),
			new SenderConfig(2)
		);
		String[] params = new String[]{
			"-" + ECHO_SERVER_PORT, expected.getEchoConnectionPoolConfig().getPort() + "",
			"-" + ECHO_SERVER_ADDRESS, expected.getEchoConnectionPoolConfig().getServerAddress(),
			"-" + INITIATOR_SERVER_PORT, expected.getInitiatorConnectionPoolConfig().getPort() + "",
			"-" + INITIATOR_SERVER_ADDRESS, expected.getInitiatorConnectionPoolConfig().getServerAddress(),
			"-" + LOCAL_SERVER_PORT_FOR_ECHO, expected.getEchoReceiverConfig().getPort() + "",
			"-" + LOCAL_SERVER_ADDRESS_FOR_ECHO, expected.getEchoReceiverConfig().getServerAddress(),
			"-" + LOCAL_SERVER_PORT_FOR_INITIATOR, expected.getInitiatorReceiverConfig().getPort() + "",
			"-" + LOCAL_SERVER_ADDRESS_FOR_INITIATOR, expected.getInitiatorReceiverConfig().getServerAddress(),
			"-" + ECHO_SENDER_THREADS, expected.getEchoSenderConfig().getThreads() + "",
			"-" + ECHO_RECEIVER_THREADS, expected.getEchoReceiverConfig().getThreads() + "",
			"-" + INITIATOR_SENDER_THREADS, expected.getInitiatorSenderConfig().getThreads() + "",
			"-" + INITIATOR_RECEIVER_THREADS, expected.getInitiatorReceiverConfig().getThreads() + ""
		};

		Configs actual = parser.parse(params);
		assertEquals(actual, expected);
	}

	//TODO add more test cases

}