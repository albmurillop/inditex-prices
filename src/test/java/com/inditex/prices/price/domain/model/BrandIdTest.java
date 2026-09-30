package com.inditex.prices.price.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inditex.prices.price.domain.exception.InvalidPriceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BrandIdTest {

  @Test
  void shouldCreateBrandIdWhenValueIsPositive() {
    final BrandId brandId = new BrandId(1L);

    assertThat(brandId.value()).isEqualTo(1L);
  }

  @ParameterizedTest
  @ValueSource(longs = {0L, -1L})
  void shouldThrowInvalidPriceExceptionWhenValueIsNotPositive(final long invalidValue) {
    assertThatThrownBy(() -> new BrandId(invalidValue)).isInstanceOf(InvalidPriceException.class);
  }
}
