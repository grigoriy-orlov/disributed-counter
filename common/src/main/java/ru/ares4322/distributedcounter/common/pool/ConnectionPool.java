package ru.ares4322.distributedcounter.common.pool;

import java.io.Closeable;
import java.net.Socket;

public interface ConnectionPool extends Closeable {

	Socket get();

	boolean put(Socket socket);

	void init();

	int size();
}
