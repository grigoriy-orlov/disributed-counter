package ru.ares4322.distributedcounter.initiator.pool;

import com.google.inject.AbstractModule;
import com.mycila.guice.ext.closeable.CloseableModule;
import com.mycila.guice.ext.jsr250.Jsr250Module;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.pool.ConnectionPoolConfig;
import ru.ares4322.distributedcounter.common.pool.common.ConnectionPoolImpl;

import javax.inject.Inject;
import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.*;

//TODO fix sometimes error
@Guice(modules = {
	Jsr250Module.class,
	CloseableModule.class,
	ConnectionPoolImplTest.TestModule.class
})
public class ConnectionPoolImplTest {

	private static final Logger log = getLogger(ConnectionPoolImplTest.class);

	@Inject
	private ConnectionPool pool;

	private ExecutorService executor;
	private static ServerSocket serverSocket;
	private static Set<Socket> clientSockets = new HashSet<>();
	private static ExecutorService socketServerExecutor;

	@BeforeClass
	public void init() throws Exception {
		executor = Executors.newFixedThreadPool(2);
	}

	@Test(enabled = false)
	public void test() throws Exception {
		TestTask task1 = new TestTask(pool);
		TestTask task2 = new TestTask(pool);
		executor.execute(task1);
		executor.execute(task2);

		//TODO beautify
		sleep(2000);

		assertEquals(pool.size(), 0);
		assertNotNull(task1.getSocket());
		assertNull(task2.getSocket());
	}

	@AfterClass
	public void destroy() throws Exception {
		executor.shutdown();
		serverSocket.close();
		for (Socket clientSocket : clientSockets) {
			clientSocket.close();
		}
		executor.shutdown();
		socketServerExecutor.shutdown();
	}

	private static class TestTask implements Runnable {

		private final ConnectionPool pool;
		private Socket socket;

		private TestTask(ConnectionPool pool) {
			this.pool = pool;
		}

		@Override
		public void run() {
			while ((socket = pool.get()) == null);
			try {
				sleep(5000);
			} catch (InterruptedException e) {
				log.error("test task running error", e);
			} finally {
				if (socket != null) {
					pool.put(socket);
				}
			}
		}

		public Socket getSocket() {
			return socket;
		}
	}

	public static class TestModule extends AbstractModule {

		@Override
		protected void configure() {
			final int remoteServerPort = RandomUtils.nextInt(1025, 65536);
			binder().bind(ConnectionPoolConfig.class).toInstance(new ConnectionPoolConfig("localhost", remoteServerPort, 3));
			binder().bind(ConnectionPool.class).to(ConnectionPoolImpl.class);

			//TODO find more right way for this
			socketServerExecutor = newSingleThreadExecutor();
			socketServerExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						serverSocket = ServerSocketFactory.getDefault().createServerSocket();
						serverSocket.bind(new InetSocketAddress("127.0.0.1", remoteServerPort));
						while (true) {
							clientSockets.add(serverSocket.accept());
						}
					} catch (IOException e) {
						log.error("initiator connection pool init error", e);
					}
				}
			});
		}
	}
}