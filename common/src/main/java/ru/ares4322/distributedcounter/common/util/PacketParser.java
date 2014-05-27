package ru.ares4322.distributedcounter.common.util;

import ru.ares4322.distributedcounter.common.domain.Packet;

import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOfRange;
import static ru.ares4322.distributedcounter.common.util.Utils.intToNetworkByteArray;
import static ru.ares4322.distributedcounter.common.util.Utils.networkByteArrayToInt;

public class PacketParser {

	public static byte[] packetToBytes(Packet packet) {
		byte[] result = new byte[Packet.size];
		arraycopy(intToNetworkByteArray(packet.getState()), 0, result, 0, 4);
		arraycopy(intToNetworkByteArray(packet.getNumber()), 0, result, 4, 4);
		return result;
	}

	public static Packet bytesToPacket(byte[] bytes) {
		return new Packet(
			networkByteArrayToInt(copyOfRange(bytes, 0, 4)),
			networkByteArrayToInt(copyOfRange(bytes, 4, 8))
		);
	}
}
