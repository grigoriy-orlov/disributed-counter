package ru.ares4322.distributedcounter.common.sorter.common;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.sorter.WriterTask;

import java.io.IOException;
import java.io.Writer;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

public class WriterTaskImpl implements WriterTask {

	private static final Logger log = getLogger(WriterTaskImpl.class);

	private final Writer writer;

	private int from;
	private int length;

	public WriterTaskImpl(
		Writer writer
	) {
		this.writer = writer;
	}

	@Override
	public void run() {
		log.debug("write to disk");
		int i = from;
		int to = from + length;
		while (i < to) {
			try {
				//TODO replace by buffer writer and append()
				writer.write(i + "\n");
				writer.flush();
			} catch (IOException e) {
				log.error(format("number %d writing error", i), e);
			}
			i++;
		}
	}

	@Override
	public void setInterval(int from, int length) {
		this.from = from;
		this.length = length;
	}
}
