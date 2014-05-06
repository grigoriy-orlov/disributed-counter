package ru.ares4322.distributedcounter.initiator;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.CounterReceiverTask;

import java.io.FileOutputStream;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.valueOf;
import static org.slf4j.LoggerFactory.getLogger;
import static ru.ares4322.distributedcounter.common.Utils.networkByteArrayToInt;

public class CounterReceiverTaskImpl implements CounterReceiverTask {

	private static final Logger log = getLogger(CounterReceiverServiceImpl.class);

	private byte[] data;
	private FileOutputStream stream;


	@Override
	public void run() {
		checkNotNull(data, "data must be not null");
		checkNotNull(stream, "stream must be not null");

		log.debug("run");
		try {
			String num = valueOf(networkByteArrayToInt(data));
			log.debug("write num to file (value = {})", num);

			stream.write(num.getBytes());
			stream.write("\n".getBytes());
			stream.flush();        //TODO remove
		} catch (IOException e) {
			log.error("can't write data to file");
		}
	}

	@Override
	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public void setStream(FileOutputStream stream) {
		this.stream = stream;
	}
}
