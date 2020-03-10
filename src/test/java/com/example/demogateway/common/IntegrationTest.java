package com.example.demogateway.common;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Tag;

@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Tag("Integration")
public @interface IntegrationTest {
}
