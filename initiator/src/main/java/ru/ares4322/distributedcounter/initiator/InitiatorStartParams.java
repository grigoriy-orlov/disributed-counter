package ru.ares4322.distributedcounter.initiator;

import java.nio.file.Path;

public class InitiatorStartParams {

	private int senderThreads;
	private int receiverThreads;
	private int serverPort;
	private String serverAddress;
	private Path senderFilePath;
	private Path receiverFilePath;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof InitiatorStartParams)) return false;

		InitiatorStartParams that = (InitiatorStartParams) o;

		if (receiverThreads != that.receiverThreads) return false;
		if (senderThreads != that.senderThreads) return false;
		if (serverPort != that.serverPort) return false;
		if (receiverFilePath != null ? !receiverFilePath.equals(that.receiverFilePath) : that.receiverFilePath != null)
			return false;
		if (senderFilePath != null ? !senderFilePath.equals(that.senderFilePath) : that.senderFilePath != null)
			return false;
		if (serverAddress != null ? !serverAddress.equals(that.serverAddress) : that.serverAddress != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = senderThreads;
		result = 31 * result + receiverThreads;
		result = 31 * result + serverPort;
		result = 31 * result + (serverAddress != null ? serverAddress.hashCode() : 0);
		result = 31 * result + (senderFilePath != null ? senderFilePath.hashCode() : 0);
		result = 31 * result + (receiverFilePath != null ? receiverFilePath.hashCode() : 0);
		return result;
	}

	public Path getReceiverFilePath() {
		return receiverFilePath;
	}

	public void setReceiverFilePath(Path receiverFilePath) {
		this.receiverFilePath = receiverFilePath;
	}

	public Path getSenderFilePath() {
		return senderFilePath;
	}

	public void setSenderFilePath(Path senderFilePath) {
		this.senderFilePath = senderFilePath;
	}

	public int getSenderThreads() {
		return senderThreads;
	}

	public void setSenderThreads(int senderThreads) {
		this.senderThreads = senderThreads;
	}

	public int getReceiverThreads() {
		return receiverThreads;
	}

	public void setReceiverThreads(int receiverThreads) {
		this.receiverThreads = receiverThreads;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	@Override
	public String toString() {
		return "InitiatorStartParams{" +
			"senderThreads=" + senderThreads +
			", receiverThreads=" + receiverThreads +
			", serverPort=" + serverPort +
			", serverAddress='" + serverAddress + '\'' +
			", senderFilePath='" + senderFilePath + '\'' +
			", receiverFilePath='" + receiverFilePath + '\'' +
			'}';
	}
}
