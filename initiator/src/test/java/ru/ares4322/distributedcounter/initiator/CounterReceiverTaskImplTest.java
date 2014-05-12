package ru.ares4322.distributedcounter.initiator;

import org.testng.annotations.Test;

import java.io.Writer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static ru.ares4322.distributedcounter.common.Utils.intToNetworkByteArray;

public class CounterReceiverTaskImplTest {

	@Test
	public void run() throws Exception {
		byte[] data = intToNetworkByteArray(10000);
		Writer mock = mock(Writer.class);
		CounterReceiverTaskImpl task = new CounterReceiverTaskImpl();
		task.setData(data);
		task.setWriter(mock);
		task.run();

		verify(mock).write("10000" + "\n");
	}
}