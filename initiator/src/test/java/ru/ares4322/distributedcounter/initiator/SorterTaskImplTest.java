package ru.ares4322.distributedcounter.initiator;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import ru.ares4322.distributedcounter.common.CounterReceiverQueue;
import ru.ares4322.distributedcounter.common.SorterTask;
import ru.ares4322.distributedcounter.common.WriterExecutor;
import ru.ares4322.distributedcounter.common.WriterTask;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static com.beust.jcommander.internal.Lists.newLinkedList;
import static java.util.Collections.shuffle;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@Guice(
	modules = {
		SorterTaskImplTest.TestModule.class
	}
)
public class SorterTaskImplTest {

	@Inject
	private SorterTask sorterTask;

	@CounterReceiverQueue
	@Inject
	private Queue<Integer> queue;

	@Inject
	@WriterExecutor
	private ExecutorService executor;

	private static final int QUEUE_LENGTH = 1000;

	@BeforeMethod
	public void setUp() throws Exception {
		LinkedList<Integer> list = newLinkedList();
		for (int i = 0; i < QUEUE_LENGTH; i++) {
			list.add(i);
		}
		shuffle(list);
		queue.addAll(list);
	}

	@Test
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

		}

		@Provides
		public SorterTask getSorterTask(Injector injector) {
			SorterTaskImpl sorterTask = new SorterTaskImpl();
			injector.injectMembers(sorterTask);
			return sorterTask;
		}

		@Provides
		public WriterTask getWriterTask() {
			return mock(WriterTask.class);
		}

		@Provides
		@CounterReceiverQueue
		@Singleton
		public Queue<Integer> getQueue() {
			return new ArrayDeque<>(QUEUE_LENGTH);
		}

		@Provides
		@Singleton
		@WriterExecutor
		public ExecutorService getWriterExecutor() {
			return mock(ExecutorService.class);
		}
	}
}