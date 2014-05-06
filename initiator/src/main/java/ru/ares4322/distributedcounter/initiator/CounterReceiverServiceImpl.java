package ru.ares4322.distributedcounter.initiator;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.CounterReceiverExecutor;
import ru.ares4322.distributedcounter.common.CounterReceiverService;
import ru.ares4322.distributedcounter.common.CounterReceiverTask;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

import static java.lang.System.arraycopy;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;
import static java.nio.channels.ServerSocketChannel.open;
import static java.nio.channels.spi.SelectorProvider.provider;
import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.slf4j.LoggerFactory.getLogger;

@Singleton
public class CounterReceiverServiceImpl extends AbstractExecutionThreadService implements CounterReceiverService {

	private static final Logger log = getLogger(CounterReceiverServiceImpl.class);

	@Inject
	private InitiatorConfig config;

	@Inject
	@CounterReceiverExecutor
	private ExecutorService executor;

	@Inject
	private Provider<CounterReceiverTaskImpl> counterReceiverTaskProvider;

	private ServerSocketChannel serverChannel;
	private Selector selector;
	private ByteBuffer readBuffer = allocate(8192);    //TODO set appropriate size
	private FileOutputStream stream;
	private boolean inProgress;

	@PostConstruct
	@Override
	public void startUp() throws IllegalStateException {
		log.debug("startUp");
		inProgress = true;

		try {
			this.selector = this.initSelector();
			stream = new FileOutputStream(config.getReceiverFilePath().toFile());
		} catch (IOException e) {
			log.error("counter receiver init selector error", e);
			throw new IllegalStateException("counter receiver starting error");
		}
	}

	@Override
	public void shutDown() {
		log.debug("shutDown");
		inProgress = false;

		if (executor != null) {
			executor.shutdown();
		}
		closeQuietly(stream);
		closeQuietly(selector);
	}

	//TODO make more concise way of exception handling
	@Override
	public void run() {
		while (inProgress) {
			log.debug("poll selector");
			try {
				selector.select();

				Iterator selectedKeys = selector.selectedKeys().iterator();
				while (selectedKeys.hasNext()) {
					SelectionKey key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					}

					if (key.isAcceptable()) {
						this.accept(key);
					} else if (key.isReadable()) {
						this.read(key);
					}
				}
			} catch (ClosedSelectorException e) {        //TODO add more cases for norm exit
				log.error("selector has closed", e);
				this.shutDown();
				return;
			} catch (IOException e) {
				log.error("polling error", e);
			}
		}
		log.debug("finish counter receiving");
	}

	private Selector initSelector() throws IOException {
		log.debug("init selector");

		Selector socketSelector = provider().openSelector();

		serverChannel = open();
		serverChannel.configureBlocking(false);

		InetSocketAddress isa = new InetSocketAddress(config.getLocalServerAddress(), config.getLocalServerPort());
		serverChannel.socket().bind(isa);

		serverChannel.register(socketSelector, OP_ACCEPT);

		return socketSelector;
	}

	private void accept(SelectionKey key) throws IOException {
		log.debug("accept connection");

		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);

		socketChannel.register(selector, OP_READ);
	}

	private void read(SelectionKey key) throws IOException {
		log.debug("read data");

		SocketChannel socketChannel = (SocketChannel) key.channel();

		readBuffer.clear();

		int numRead;
		try {
			numRead = socketChannel.read(readBuffer);
		} catch (IOException e) {
			log.error("socket reading error", e);
			key.cancel();
			socketChannel.close();
			return;
		}

		if (numRead == -1) {
			key.channel().close();
			key.cancel();
			return;
		}

		int i = 0;
		//TODO beautify
		while (numRead >= 4) {
			byte[] data = new byte[4];
			arraycopy(readBuffer.array(), i, data, 0, 4);
			CounterReceiverTask task = counterReceiverTaskProvider.get();
			task.setData(data);
			task.setStream(stream);
			executor.execute(task);
			i += 4;
			numRead -= 4;
		}
	}

}
