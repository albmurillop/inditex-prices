package com.inditex.prices.price.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inditex.prices.price.domain.exception.InvalidPriceException;
import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;

class MoneyTest {

  private static final Currency EUR = Currency.getInstance("EUR");

  @Test
  void shouldNormalizeAmountScaleWhenCreatingMoney() {
    final Money money = new Money(new BigDecimal("35.5"), EUR);

    assertThat(money.amount()).isEqualByComparingTo("35.50");
    assertThat(money.amount().scale()).isEqualTo(2);
    assertThat(money.currency()).isEqualTo(EUR);
  }

  @Test
  void shouldThrowInvalidPriceExceptionWhenAmountIsNull() {
    assertThatThrownBy(() -> new Money(null, EUR)).isInstanceOf(InvalidPriceException.class);
  }

  @Test
  void shouldThrowInvalidPriceExceptionWhenCurrencyIsNull() {
    assertThatThrownBy(() -> new Money(BigDecimal.TEN, null))
        .isInstanceOf(InvalidPriceException.class);
  }

  @Test
  void shouldThrowInvalidPriceExceptionWhenAmountIsNegative() {
    assertThatThrownBy(() -> new Money(new BigDecimal("-0.01"), EUR))
        .isInstanceOf(InvalidPriceException.class);
  }
}
