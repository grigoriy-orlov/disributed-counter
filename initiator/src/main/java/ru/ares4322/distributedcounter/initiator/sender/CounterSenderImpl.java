package ru.ares4322.distributedcounter.initiator.sender;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.sender.CounterSender;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.ares4322.distributedcounter.common.Utils.intToNetworkByteArray;

//TODO Singleton is right?
class CounterSenderImpl implements CounterSender {

	private static final Logger log = getLogger(CounterSenderImpl.class);

	@Inject
	private ConnectionPool pool;

	@Override
	public void send(int counter) {
		Socket socket;
		while ((socket = pool.get()) == null) {
		}

		try {
			OutputStream outputStream = socket.getOutputStream();
			log.debug("send data");

			byte[] bytes;
			bytes = intToNetworkByteArray(counter);
			outputStream.write(bytes);
			outputStream.flush();  //TODO remove
			//TODO need close?
		} catch (IOException e) {
			log.error("socket writing error", e);
		} finally {
			pool.put(socket);
		}
	}

}
