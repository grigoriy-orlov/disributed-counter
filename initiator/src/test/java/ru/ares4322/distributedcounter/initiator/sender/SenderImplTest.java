package ru.ares4322.distributedcounter.initiator.sender;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import org.apache.commons.lang3.RandomUtils;
import org.testng.IModuleFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import ru.ares4322.distributedcounter.common.domain.Packet;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.sender.Sender;
import ru.ares4322.distributedcounter.initiator.QueueModule;
import ru.ares4322.distributedcounter.initiator.cfg.ConfigModule;

import javax.inject.Inject;
import java.io.OutputStream;
import java.net.Socket;

import static org.mockito.Mockito.*;
import static ru.ares4322.distributedcounter.common.util.PacketParser.packetToBytes;
import static ru.ares4322.distributedcounter.initiator.cfg.CliParams.*;

@Guice(
	modules = {
		SenderModule.class,
		QueueModule.class,
		SenderImplTest.TestModule.class
	},
	moduleFactory = SenderImplTest.ConfigModuleFactory.class
)
public class SenderImplTest {

	@Inject
	private ConnectionPool pool;

	@Inject
	private Sender sender;

	@BeforeMethod
	public void setUp() throws Exception {
		reset(pool);
	}

	@AfterMethod
	public void tearDown() throws Exception {

	}

	@Test
	public void send() throws Exception {
		Packet packet = new Packet(1, 10000);
		Socket socket = mock(Socket.class);
		OutputStream stream = mock(OutputStream.class);

		when(socket.getOutputStream()).thenReturn(stream);
		when(pool.get()).thenReturn(socket);
		sender.send(packet);

		verify(stream).write(packetToBytes(packet));
	}

	public static class TestModule extends AbstractModule {

		@Override
		protected void configure() {
			binder().bind(ConnectionPool.class).toInstance(mock(ConnectionPool.class));
		}
	}

	public static class ConfigModuleFactory implements IModuleFactory {

		@Override
		public Module createModule(ITestContext context, Class<?> testClass) {
			int port = RandomUtils.nextInt(20000, 40000);
			return new ConfigModule(
				new String[]{
					"-" + LOCAL_SERVER_PORT, port + "",
					"-" + LOCAL_SERVER_ADDRESS, "127.0.0.1",
					"-" + REMOTE_SERVER_PORT, port + "",
					"-" + REMOTE_SERVER_ADDRESS, "127.0.0.1",
					"-" + SENDER_THREADS, "3",
					"-" + RECEIVER_THREADS, "3"
				}
			);
		}
	}
}