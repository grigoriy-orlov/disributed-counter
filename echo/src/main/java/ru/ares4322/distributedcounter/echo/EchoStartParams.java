package ru.ares4322.distributedcounter.echo;

public class EchoStartParams {

	private int senderThreads;
	private int receiverThreads;
	private int serverPort;
	private String serverAddress;
	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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
		return "EchoStartParams{" +
			"senderThreads=" + senderThreads +
			", receiverThreads=" + receiverThreads +
			", serverPort=" + serverPort +
			", serverAddress='" + serverAddress + '\'' +
			", filePath='" + filePath + '\'' +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof EchoStartParams)) return false;

		EchoStartParams that = (EchoStartParams) o;

		if (receiverThreads != that.receiverThreads) return false;
		if (senderThreads != that.senderThreads) return false;
		if (serverPort != that.serverPort) return false;
		if (filePath != null ? !filePath.equals(that.filePath) : that.filePath != null) return false;
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
		result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
		return result;
	}

}
