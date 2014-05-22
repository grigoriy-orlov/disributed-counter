package ru.ares4322.distributedcounter.initiator.receiver;

import org.testng.annotations.Test;
import ru.ares4322.distributedcounter.initiator.receiver.CounterReceiverTaskImpl;

import java.io.Writer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static ru.ares4322.distributedcounter.common.Utils.intToNetworkByteArray;

public class CounterReceiverTaskImplTest {

	@Test(enabled = false)
	public void run() throws Exception {
		byte[] data = intToNetworkByteArray(10000);
		Writer mock = mock(Writer.class);
		CounterReceiverTaskImpl task = new CounterReceiverTaskImpl();
		task.setData(data);
		task.run();

		verify(mock).write("10000" + "\n");
	}
}