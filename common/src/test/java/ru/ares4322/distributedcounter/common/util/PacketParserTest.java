package ru.ares4322.distributedcounter.common.util;

import org.testng.annotations.Test;
import ru.ares4322.distributedcounter.common.domain.Packet;

import static org.testng.Assert.assertEquals;
import static ru.ares4322.distributedcounter.common.util.PacketParser.bytesToPacket;
import static ru.ares4322.distributedcounter.common.util.PacketParser.packetToBytes;

public class PacketParserTest {

	@Test
	public void testPacketToBytes() throws Exception {
		Packet expected = new Packet(123, 456);
		Packet actual = bytesToPacket(packetToBytes(expected));
		assertEquals(actual, expected);
	}

	@Test
	public void testBytesToPacket() throws Exception {
		byte[] bytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
		byte[] bytes1 = packetToBytes(bytesToPacket(bytes));
		assertEquals(bytes, bytes1);
	}
}