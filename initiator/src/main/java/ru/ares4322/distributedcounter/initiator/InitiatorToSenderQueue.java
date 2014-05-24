package ru.ares4322.distributedcounter.initiator;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Qualifier
@Retention(RUNTIME)
@Target({FIELD, METHOD, PARAMETER})
public @interface InitiatorToSenderQueue {
}
