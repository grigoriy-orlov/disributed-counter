package ru.ares4322.distributedcounter.echo;

import com.google.inject.ImplementedBy;
import ru.ares4322.distributedcounter.common.StartParamsParserException;

@ImplementedBy(EchoStartParamsParserImpl.class)
public interface EchoStartParamsParser {

	EchoStartParams parse(String[] params) throws StartParamsParserException;
}
