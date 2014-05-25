package ru.ares4322.distributedcounter.common.sorter;

import java.nio.file.Path;

public class WriterConfig {

	private final Path filePath;

	public WriterConfig(Path filePath) {
		this.filePath = filePath;
	}

	public Path getFilePath() {
		return filePath;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WriterConfig)) return false;

		WriterConfig that = (WriterConfig) o;

		if (filePath != null ? !filePath.equals(that.filePath) : that.filePath != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return filePath != null ? filePath.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "WriterConfig{" +
			"filePath=" + filePath +
			'}';
	}
}
