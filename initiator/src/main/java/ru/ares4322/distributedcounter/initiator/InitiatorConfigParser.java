package ru.ares4322.distributedcounter.initiator;

import com.google.inject.ImplementedBy;
import ru.ares4322.distributedcounter.common.StartParamsParserException;

@ImplementedBy(InitiatorConfigParserImpl.class)
public interface InitiatorConfigParser {

	InitiatorConfig parse(String[] params) throws StartParamsParserException;
}
