package com.inditex.prices.price.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inditex.prices.price.domain.exception.InvalidPriceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductIdTest {

  @Test
  void shouldCreateProductIdWhenValueIsPositive() {
    final ProductId productId = new ProductId(35455L);

    assertThat(productId.value()).isEqualTo(35455L);
  }

  @ParameterizedTest
  @ValueSource(longs = {0L, -1L})
  void shouldThrowInvalidPriceExceptionWhenValueIsNotPositive(final long invalidValue) {
    assertThatThrownBy(() -> new ProductId(invalidValue)).isInstanceOf(InvalidPriceException.class);
  }
}
