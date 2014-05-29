package ru.ares4322.distributedcounter.common.pool.common;

import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.pool.ConnectionPool;
import ru.ares4322.distributedcounter.common.pool.ConnectionPoolConfig;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.net.SocketFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.net.InetAddress.getByName;
import static org.slf4j.LoggerFactory.getLogger;


public class ConnectionPoolImpl implements ConnectionPool {

	private static final Logger log = getLogger(ConnectionPoolImpl.class);

	private final ConnectionPoolConfig config;

	private BlockingQueue<Socket> queue;

	private int state; // 1 - inited, 2 - run, 3 - closed

	@Inject
	public ConnectionPoolImpl(ConnectionPoolConfig config) {
		this.config = config;
	}

	@PostConstruct
	public void init() {
		state = 1;
		queue = new ArrayBlockingQueue<>(config.getSize());
	}

	@Override
	public void start() {
		if (state != 2) {
			state = 2;
			for (int i = 0, l = config.getSize(); i < l; i++) {
				try {
					Socket socket = SocketFactory.getDefault().createSocket();
					socket.connect(new InetSocketAddress(getByName(config.getServerAddress()), config.getPort()));
					queue.put(socket);
				} catch (IOException e) {
					log.error("socket creating error", e);
				} catch (InterruptedException e) {
					log.error("socket pool adding error", e);
				}
			}
		}
	}

	@Override
	//TODO add @Nullable to API
	//TODO maybe poll -> take?
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
		state = 3;
		for (Socket socket : queue) {
			try {
				socket.close();
			} catch (IOException e) {
				log.error("can't close socket");
			}
		}

	}
}
