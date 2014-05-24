package ru.ares4322.distributedcounter.common.sender.common;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.sender.Sender;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.ares4322.distributedcounter.common.util.Utils.intToNetworkByteArray;

public class SenderImpl implements Sender {

	private static final Logger log = getLogger(SenderImpl.class);

	private final ConnectionPool pool;

	@Inject
	public SenderImpl(ConnectionPool pool) {
		this.pool = pool;
	}

	@Override
	public void send(int counter) {
		Socket socket;
		//FIXME poll->take
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
