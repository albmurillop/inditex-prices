package com.inditex.prices.price.domain.model;

import com.inditex.prices.price.domain.exception.InvalidPriceException;
import java.time.LocalDateTime;

public record ApplicationPeriod(LocalDateTime start, LocalDateTime end) {

  public ApplicationPeriod {
    if (start == null) {
      throw new InvalidPriceException("Start date must not be null.");
    }
    if (end == null) {
      throw new InvalidPriceException("End date must not be null.");
    }
    if (end.isBefore(start)) {
      throw new InvalidPriceException("End date must not be before the start date.");
    }
  }
}
