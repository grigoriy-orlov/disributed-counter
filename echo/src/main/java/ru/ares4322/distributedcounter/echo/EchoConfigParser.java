package ru.ares4322.distributedcounter.echo;

import com.google.inject.ImplementedBy;
import ru.ares4322.distributedcounter.common.cfg.StartParamsParserException;

@ImplementedBy(EchoConfigParserImpl.class)
public interface EchoConfigParser {

	EchoConfig parse(String[] params) throws StartParamsParserException;
}
