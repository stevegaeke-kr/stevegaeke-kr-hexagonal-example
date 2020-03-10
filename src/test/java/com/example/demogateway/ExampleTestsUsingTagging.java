package com.example.demogateway;

import com.example.demogateway.common.IntegrationTest;
import com.example.demogateway.common.EndToEndTest;
import com.example.demogateway.common.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

// Examples of using the appropriate attribute for @UnitTest, @IntegrationTest or @EndToEndTest

@UnitTest
class ExampleUnitTest {

  @DisplayName("Example unit test showing the class level attribute.")
  @Test
  void shouldPass() {
    assertTrue(true);
  }
}

@IntegrationTest
class ExampleIntegrationTest {

  @DisplayName("Example integration test showing the class level attribute.")
  @Test
  void shouldPass() {
    assertTrue(true);
  }
}

@EndToEndTest
class ExampleEndToEndTest {

  @DisplayName("Example system test showing the class level attribute.")
  @Test
  void shouldPass() {
    assertTrue(true);
  }
}
