package ru.ares4322.distributedcounter.initiator.sorter;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import ru.ares4322.distributedcounter.common.sorter.ReceiverWriter;
import ru.ares4322.distributedcounter.common.sorter.WriterTask;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Writer;

import static org.mockito.Mockito.*;

@Guice(
	modules = {
		ReceiverSorterModule.class,
		WriterTaskImplTest.TestModule.class
	}
)
public class WriterTaskImplTest {

	@Inject
	private WriterTask writerTask;

	@Inject
	@ReceiverWriter
	private Writer writer;

	@Test
	public void test() throws Exception {
		int from = 123;
		int length = 53;
		writerTask.setInterval(from, length);

		writerTask.run();

		verify(writer, times(53));
		for (int i = from; i < length; i++) {
			verify(writer).write(i + "\n");
		}
	}

	public static class TestModule extends AbstractModule {

		@Override
		protected void configure() {
		}

		@Provides
		@Singleton
		@ReceiverWriter
		public Writer getWriter() {
			return mock(Writer.class);
		}
	}
}