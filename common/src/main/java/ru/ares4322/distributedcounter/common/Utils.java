package ru.ares4322.distributedcounter.common;

import java.nio.ByteBuffer;

import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteOrder.BIG_ENDIAN;

public class Utils {

	public static byte[] intToNetworkByteArray(int counter) {
		ByteBuffer buffer = allocate(4);
		buffer.order(BIG_ENDIAN);
		buffer.putInt(counter);
		return buffer.array();
	}

	public static int networkByteArrayToInt(byte[] bytes) {
		ByteBuffer buffer = allocate(4);
		buffer.order(BIG_ENDIAN);
		buffer.put(bytes);
		buffer.flip();
		return buffer.getInt();
	}
}
