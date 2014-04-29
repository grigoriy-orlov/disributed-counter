package ru.ares4322.distributedcounter.initiator;

import com.google.inject.AbstractModule;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import ru.ares4322.distributedcounter.common.ConnectionPool;
import ru.ares4322.distributedcounter.common.CounterSender;

import javax.inject.Inject;
import java.io.OutputStream;
import java.net.Socket;

import static org.mockito.Mockito.*;
import static ru.ares4322.distributedcounter.common.Utils.intToNetworkByteArray;

@Guice(modules = CounterSenderImplTest.TestModule.class)
public class CounterSenderImplTest {

	@Inject
	private ConnectionPool pool;

	@Inject
	private CounterSender sender;

	@BeforeMethod
	public void setUp() throws Exception {
		reset(pool);
	}

	@AfterMethod
	public void tearDown() throws Exception {

	}

	@Test
	public void send() throws Exception {
		int counter = 10000;
		Socket socket = mock(Socket.class);
		OutputStream stream = mock(OutputStream.class);

		when(socket.getOutputStream()).thenReturn(stream);
		when(pool.get()).thenReturn(socket);
		sender.send(counter);

		verify(stream).write(intToNetworkByteArray(counter));
	}

	public static class TestModule extends AbstractModule {

		@Override
		protected void configure() {
			binder().bind(ConnectionPool.class).toInstance(mock(ConnectionPool.class));
		}
	}
}