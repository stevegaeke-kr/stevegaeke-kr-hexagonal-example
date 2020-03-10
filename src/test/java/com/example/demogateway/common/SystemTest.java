package com.example.demogateway.common;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Tag;

@Target(TYPE)
@Retention(RUNTIME)
@Tag("System")
public @interface SystemTest {
}
