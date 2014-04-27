package ru.ares4322.distributedcounter.initiator;

import com.google.inject.ImplementedBy;
import ru.ares4322.distributedcounter.common.StartParamsParserException;

@ImplementedBy(InitiatorStartParamsParserImpl.class)
public interface InitiatorStartParamsParser {

	InitiatorStartParams parse(String[] params) throws StartParamsParserException;
}
