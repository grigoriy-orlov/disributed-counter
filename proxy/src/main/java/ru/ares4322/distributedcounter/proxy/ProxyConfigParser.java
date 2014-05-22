package ru.ares4322.distributedcounter.proxy;

import com.google.inject.ImplementedBy;
import ru.ares4322.distributedcounter.common.cfg.StartParamsParserException;

@ImplementedBy(ProxyConfigParserImpl.class)
public interface ProxyConfigParser {

	ProxyConfig parse(String[] params) throws StartParamsParserException;
}
