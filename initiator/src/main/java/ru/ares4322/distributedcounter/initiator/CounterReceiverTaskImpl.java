package ru.ares4322.distributedcounter.initiator;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.CounterReceiverTask;

import java.io.IOException;
import java.io.Writer;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.valueOf;
import static org.slf4j.LoggerFactory.getLogger;
import static ru.ares4322.distributedcounter.common.Utils.networkByteArrayToInt;

public class CounterReceiverTaskImpl implements CounterReceiverTask {

	private static final Logger log = getLogger(CounterReceiverServiceImpl.class);

	private byte[] data;
	private Writer writer;


	@Override
	public void run() {
		checkNotNull(data, "data must be not null");
		checkNotNull(writer, "writer must be not null");

		log.debug("run");
		try {
			String num = valueOf(networkByteArrayToInt(data));
			log.debug("write num to file (value = {})", num);
			writer.write(num + "\n");
			writer.flush();
		} catch (IOException e) {
			log.error("can't write data to file");
		}
	}

	@Override
	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public void setWriter(Writer writer) {
		this.writer = writer;
	}


}
