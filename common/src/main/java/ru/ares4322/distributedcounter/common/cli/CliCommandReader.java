package ru.ares4322.distributedcounter.common.cli;

import com.google.inject.ImplementedBy;

@ImplementedBy(CliCommandReaderImpl.class)
public interface CliCommandReader {

	void readCommand();

	boolean isExit();
}
