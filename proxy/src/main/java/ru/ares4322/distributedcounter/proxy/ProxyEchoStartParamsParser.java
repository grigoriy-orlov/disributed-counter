package ru.ares4322.distributedcounter.proxy;

import com.google.inject.ImplementedBy;
import ru.ares4322.distributedcounter.common.StartParamsParserException;

@ImplementedBy(ProxyStartParamsParserImpl.class)
public interface ProxyEchoStartParamsParser {

	ProxyStartParams parse(String[] params) throws StartParamsParserException;
}
