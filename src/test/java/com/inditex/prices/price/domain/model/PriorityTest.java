package com.inditex.prices.price.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inditex.prices.price.domain.exception.InvalidPriceException;
import org.junit.jupiter.api.Test;

class PriorityTest {

  @Test
  void shouldCreatePriorityWhenValueIsPositive() {
    final Priority priority = new Priority(1);

    assertThat(priority.value()).isEqualTo(1);
  }

  @Test
  void shouldCreatePriorityWhenValueIsZero() {
    final Priority priority = new Priority(0);

    assertThat(priority.value()).isZero();
  }

  @Test
  void shouldThrowInvalidPriceExceptionWhenValueIsNegative() {
    assertThatThrownBy(() -> new Priority(-1)).isInstanceOf(InvalidPriceException.class);
  }
}
