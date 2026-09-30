package com.inditex.prices.price.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inditex.prices.price.domain.exception.InvalidPriceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PriceListIdTest {

  @Test
  void shouldCreatePriceListIdWhenValueIsPositive() {
    final PriceListId priceListId = new PriceListId(1L);

    assertThat(priceListId.value()).isEqualTo(1L);
  }

  @ParameterizedTest
  @ValueSource(longs = {0L, -1L})
  void shouldThrowInvalidPriceExceptionWhenValueIsNotPositive(final long invalidValue) {
    assertThatThrownBy(() -> new PriceListId(invalidValue))
        .isInstanceOf(InvalidPriceException.class);
  }
}
