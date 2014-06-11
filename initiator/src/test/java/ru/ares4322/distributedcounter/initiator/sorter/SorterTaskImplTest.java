package ru.ares4322.distributedcounter.initiator.sorter;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import ru.ares4322.distributedcounter.common.domain.Packet;
import ru.ares4322.distributedcounter.common.pool.ConnectionPoolConfig;
import ru.ares4322.distributedcounter.common.sender.SenderConfig;
import ru.ares4322.distributedcounter.common.sorter.SorterTask;
import ru.ares4322.distributedcounter.common.sorter.WriterExecutor;
import ru.ares4322.distributedcounter.common.sorter.WriterTask;
import ru.ares4322.distributedcounter.common.sorter.common.SorterTaskImpl;
import ru.ares4322.distributedcounter.initiator.QueueModule;
import ru.ares4322.distributedcounter.initiator.ReceiverToExitorQueue;
import ru.ares4322.distributedcounter.initiator.pool.ConnectionPoolModule;
import ru.ares4322.distributedcounter.initiator.sender.SenderModule;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static com.beust.jcommander.internal.Lists.newLinkedList;
import static java.util.Collections.shuffle;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@Guice(
	modules = {
		SorterTaskImplTest.TestModule.class,
		SenderModule.class,
		ConnectionPoolModule.class,
		QueueModule.class
	}
)
public class SorterTaskImplTest {

	@Inject
	private SorterTask sorterTask;

	@ReceiverToExitorQueue
	@Inject
	private BlockingQueue<Packet> queue;

	@Inject
	@WriterExecutor
	private ExecutorService executor;

	private static final int QUEUE_LENGTH = 1000;

	@BeforeMethod
	public void setUp() throws Exception {
		LinkedList<Packet> list = newLinkedList();
		for (int i = 0; i < QUEUE_LENGTH; i++) {
			list.add(new Packet(-1, i));
		}
		shuffle(list);
		queue.addAll(list);
	}

	@Test(enabled = false)
	public void testFinish() throws Exception {
		ExecutorService exitExecutor = newSingleThreadExecutor();
		exitExecutor.submit(new Callable<Integer>() {
			@Override
			public Integer call() {
				int a = 0, b = 0;
				while (queue.peek() != null) {
					b = a++;
				}
				verify(executor, never()).execute(any(Runnable.class));
				sorterTask.exit();
				return 1;
			}
		});

		sorterTask.run();
		verify(executor, times(1)).execute(any(Runnable.class));


		exitExecutor.shutdown();
	}

	public static class TestModule extends AbstractModule {

		@Override
		protected void configure() {
			binder()
				.bind(ConnectionPoolConfig.class)
				.toInstance(new ConnectionPoolConfig("localhost", 8888, 3));

			binder()
				.bind(SenderConfig.class)
				.toInstance(new SenderConfig(3));
		}

		@Provides
		public SorterTask getSorterTask(
			@ReceiverToExitorQueue BlockingQueue<Packet> queue,
			@WriterExecutor ExecutorService executor,
			Provider<WriterTask> writerTaskProvider
		) {
			SorterTaskImpl sorterTask = new SorterTaskImpl(queue, executor, writerTaskProvider);
			return sorterTask;
		}

		@Provides
		public WriterTask getWriterTask() {
			return mock(WriterTask.class);
		}

		@Provides
		@Singleton
		@WriterExecutor
		public ExecutorService getWriterExecutor() {
			return mock(ExecutorService.class);
		}
	}
}