package com.inditex.prices.price.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inditex.prices.price.domain.exception.InvalidPriceException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ApplicationPeriodTest {

  private static final LocalDateTime START = LocalDateTime.of(2020, 6, 14, 0, 0, 0);
  private static final LocalDateTime END = LocalDateTime.of(2020, 12, 31, 23, 59, 59);

  @Test
  void shouldCreatePeriodWhenRangeIsValid() {
    final ApplicationPeriod period = new ApplicationPeriod(START, END);

    assertThat(period.start()).isEqualTo(START);
    assertThat(period.end()).isEqualTo(END);
  }

  @Test
  void shouldCreatePeriodWhenStartAndEndAreTheSameInstant() {
    final ApplicationPeriod period = new ApplicationPeriod(START, START);

    assertThat(period.start()).isEqualTo(period.end());
  }

  @Test
  void shouldThrowInvalidPriceExceptionWhenEndIsBeforeStart() {
    assertThatThrownBy(() -> new ApplicationPeriod(END, START))
        .isInstanceOf(InvalidPriceException.class);
  }

  @Test
  void shouldThrowInvalidPriceExceptionWhenStartIsNull() {
    assertThatThrownBy(() -> new ApplicationPeriod(null, END))
        .isInstanceOf(InvalidPriceException.class);
  }

  @Test
  void shouldThrowInvalidPriceExceptionWhenEndIsNull() {
    assertThatThrownBy(() -> new ApplicationPeriod(START, null))
        .isInstanceOf(InvalidPriceException.class);
  }
}
