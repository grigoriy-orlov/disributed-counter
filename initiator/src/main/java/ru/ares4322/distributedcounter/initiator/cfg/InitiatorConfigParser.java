package ru.ares4322.distributedcounter.initiator.cfg;

import com.google.inject.ImplementedBy;
import ru.ares4322.distributedcounter.common.cfg.StartParamsParserException;

@ImplementedBy(InitiatorConfigParserImpl.class)
public interface InitiatorConfigParser {

	InitiatorConfig parse(String[] params) throws StartParamsParserException;
}
