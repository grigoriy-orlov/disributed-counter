package ru.ares4322.distributedcounter.initiator.sorter;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import ru.ares4322.distributedcounter.common.sorter.ReceiverWriter;
import ru.ares4322.distributedcounter.common.sorter.WriterConfig;
import ru.ares4322.distributedcounter.common.sorter.WriterTask;
import ru.ares4322.distributedcounter.initiator.QueueModule;
import ru.ares4322.distributedcounter.initiator.ReceiverSorter;

import javax.inject.Inject;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.slf4j.LoggerFactory.getLogger;

@Guice(
	modules = {
		ReceiverSorterModule.class,
		WriterTaskImplTest.TestModule.class,
		QueueModule.class
	}
)
public class WriterTaskImplTest {

	private static final Logger log = getLogger(WriterTaskImplTest.class);

	@Inject
	@ReceiverSorter
	private WriterTask writerTask;

	@Inject
	@ReceiverWriter
	private Writer writer;

	@Test(enabled = false)
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
			Path tmp = null;
			try {
				tmp = Files.createTempFile("tmp", "");
			} catch (IOException e) {
				log.error("tmp file creating error", e);
			}
			bind(WriterConfig.class)
				.annotatedWith(ReceiverWriter.class)
				.toInstance(new WriterConfig(tmp));
		}
//
//		@Provides
//		@Singleton
//		@ReceiverWriter
//		public Writer getWriter() {
//			return mock(Writer.class);
//		}
	}
}