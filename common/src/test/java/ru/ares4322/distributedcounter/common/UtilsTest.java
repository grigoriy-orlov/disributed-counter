package ru.ares4322.distributedcounter.common;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static ru.ares4322.distributedcounter.common.Utils.intToNetworkByteArray;
import static ru.ares4322.distributedcounter.common.Utils.networkByteArrayToInt;

public class UtilsTest {

	@Test
	public void test() throws Exception {
		int num = 10000;
		byte[] bytes = {1, 2, 3, 4};
		assertEquals(num, networkByteArrayToInt(intToNetworkByteArray(num)));
		assertEquals(bytes, intToNetworkByteArray(networkByteArrayToInt(bytes)));
	}
}