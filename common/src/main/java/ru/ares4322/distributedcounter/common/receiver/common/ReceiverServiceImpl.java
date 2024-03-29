package ru.ares4322.distributedcounter.common.receiver.common;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import ru.ares4322.distributedcounter.common.domain.Packet;
import ru.ares4322.distributedcounter.common.receiver.ReceiverConfig;
import ru.ares4322.distributedcounter.common.receiver.ReceiverService;
import ru.ares4322.distributedcounter.common.receiver.ReceiverTask;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Provider;
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
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.slf4j.LoggerFactory.getLogger;

public class ReceiverServiceImpl implements ReceiverService {

	private static final Logger log = getLogger(ReceiverServiceImpl.class);

	private final ReceiverConfig config;
	private final ExecutorService taskExecutor;
	private final Provider<ReceiverTask> taskProvider;
	//TODO move to module
	private final ExecutorService serviceExecutor = newSingleThreadExecutor(
		new BasicThreadFactory.Builder().namingPattern("CounterReceiverService-%s").daemon(true).build()
	);
	private ServerSocketChannel serverChannel;
	private Selector selector;
	private ByteBuffer readBuffer = allocate(8192);    //TODO set appropriate size
	private boolean inProgress;

	@Inject
	public ReceiverServiceImpl(
		ReceiverConfig config,
		ExecutorService taskExecutor,
		Provider<ReceiverTask> taskProvider
	) {
		this.config = config;
		this.taskExecutor = taskExecutor;
		this.taskProvider = taskProvider;
	}

	@PostConstruct
	public void init() throws IllegalStateException {
		log.debug("init");

		try {
			this.selector = this.initSelector();
		} catch (IOException e) {
			log.error("counter receiver init selector error", e);
			throw new IllegalStateException("counter receiver starting error");
		}
	}

	@Override
	public void startUp() {
		log.debug("startUp");
		inProgress = true;
		serviceExecutor.execute(this);
	}

	public void shutDown() {
		log.debug("shutDown");
		inProgress = false;

		if (taskExecutor != null) {
			taskExecutor.shutdown();
		}
		if (serviceExecutor != null) {
			serviceExecutor.shutdown();
		}
		closeQuietly(selector);
		closeQuietly(serverChannel);
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
				log.debug("selector has closed", e);
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

		InetSocketAddress isa = new InetSocketAddress(config.getServerAddress(), config.getPort());
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
		while (numRead >= Packet.size) {
			byte[] data = new byte[Packet.size];
			arraycopy(readBuffer.array(), i, data, 0, Packet.size);
			ReceiverTask task = taskProvider.get();
			task.setData(data);
			taskExecutor.execute(task);
			i += Packet.size;
			numRead -= Packet.size;
		}
	}

}
