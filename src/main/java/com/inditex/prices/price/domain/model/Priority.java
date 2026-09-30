package com.inditex.prices.price.domain.model;

import com.inditex.prices.price.domain.exception.InvalidPriceException;

public record Priority(int value) {

  public Priority {
    if (value < 0) {
      throw new InvalidPriceException("Priority must not be negative.");
    }
  }
}
