package ru.ares4322.distributedcounter.initiator;

import com.google.inject.AbstractModule;
import com.mycila.guice.ext.closeable.CloseableModule;
import com.mycila.guice.ext.jsr250.Jsr250Module;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import ru.ares4322.distributedcounter.common.ConnectionPool;

import javax.inject.Inject;
import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.google.inject.name.Names.named;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.slf4j.LoggerFactory.getLogger;
import static org.testng.Assert.*;

//TODO fix sometimes error
@Guice(modules = {
	Jsr250Module.class,
	CloseableModule.class,
	InitiatorConnectionPoolImplTest.TestModule.class
})
public class InitiatorConnectionPoolImplTest {

	private static final Logger log = getLogger(InitiatorConnectionPoolImplTest.class);

	@Inject
	private ConnectionPool pool;

	@Inject
	private InitiatorConfig config;
	private ExecutorService executor;
	private static ServerSocket serverSocket;
	private static Set<Socket> clientSockets = new HashSet<>();
	private static ExecutorService socketServerExecutor;

	@BeforeClass
	public void init() throws Exception {
		executor = Executors.newFixedThreadPool(2);
	}

	@Test
	public void test() throws Exception {
		TestTask task1 = new TestTask(pool);
		TestTask task2 = new TestTask(pool);
		executor.execute(task1);
		executor.execute(task2);

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
			socket = pool.get();
			try {
				Thread.sleep(5000);
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
			binder().bind(Integer.class).annotatedWith(named("senderThreads")).toInstance(1);
			binder().bind(Integer.class).annotatedWith(named("receiverThreads")).toInstance(1);
			binder().bind(Integer.class).annotatedWith(named("localServerPort")).toInstance(RandomUtils.nextInt(1025, 65536));
			binder().bind(String.class).annotatedWith(named("localServerAddress")).toInstance("127.0.0.1");
			final int remoteServerPort = RandomUtils.nextInt(1025, 65536);
			binder().bind(Integer.class).annotatedWith(named("remoteServerPort")).toInstance(remoteServerPort);
			binder().bind(String.class).annotatedWith(named("remoteServerAddress")).toInstance("127.0.0.1");
			binder().bind(Path.class).annotatedWith(named("senderFilePath")).toInstance(Paths.get(""));
			binder().bind(Path.class).annotatedWith(named("receiverFilePath")).toInstance(Paths.get(""));

			binder().bind(ConnectionPool.class).to(InitiatorConnectionPoolImpl.class);

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