package ru.ares4322.distributedcounter.initiator;

import org.testng.annotations.Test;

import java.io.FileOutputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static ru.ares4322.distributedcounter.common.Utils.intToNetworkByteArray;

public class CounterReceiverTaskImplTest {

	@Test
	public void run() throws Exception {
		byte[] data = intToNetworkByteArray(10000);
		FileOutputStream mock = mock(FileOutputStream.class);
		CounterReceiverTaskImpl task = new CounterReceiverTaskImpl();
		task.setData(data);
		task.setStream(mock);
		task.run();

		verify(mock).write("10000".getBytes());
	}
}