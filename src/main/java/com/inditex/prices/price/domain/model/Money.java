package com.inditex.prices.price.domain.model;

import com.inditex.prices.price.domain.exception.InvalidPriceException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public record Money(BigDecimal amount, Currency currency) {

  private static final int SCALE = 2;

  public Money {
    if (amount == null) {
      throw new InvalidPriceException("Amount must not be null.");
    }
    if (currency == null) {
      throw new InvalidPriceException("Currency must not be null.");
    }
    if (amount.signum() < 0) {
      throw new InvalidPriceException("Amount must not be negative.");
    }
    amount = amount.setScale(SCALE, RoundingMode.HALF_UP);
  }
}
