package ru.ares4322.distributedcounter.initiator;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.ConnectionPool;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.net.InetAddress.getByName;
import static org.slf4j.LoggerFactory.getLogger;

@Singleton
public class InitiatorConnectionPoolImpl implements ConnectionPool {

	private static final Logger log = getLogger(InitiatorConnectionPoolImpl.class);

	private BlockingQueue<Socket> queue;

	@Inject
	InitiatorConfig config;

	@PostConstruct
	public void init() {
		queue = new ArrayBlockingQueue<>(config.getSenderThreads());
		for (int i = 0, l = config.getSenderThreads(); i < l; i++) {
			try {
				Socket socket = new Socket();
				socket.connect(new InetSocketAddress(getByName(config.getRemoteServerAddress()), config.getRemoteServerPort()));
				queue.put(socket);
			} catch (IOException e) {
				log.error("socket creating error", e);
			} catch (InterruptedException e) {
				log.error("socket pool adding error", e);
			}
		}
	}

	@Override
	//TODO add @Nullable to API
	public Socket get() {
		return queue.poll();
	}

	@Override
	public boolean put(Socket socket) {
		return queue.offer(socket);
	}

	public int size() {
		return queue.size();
	}

	@Override
	public void close() {
		for (Socket socket : queue) {
			try {
				socket.close();
			} catch (IOException e) {
				log.error("can't close socket");
			}
		}

	}
}
