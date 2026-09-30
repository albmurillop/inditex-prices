package com.inditex.prices.price.domain.model;

import com.inditex.prices.price.domain.exception.InvalidPriceException;

public record ProductId(long value) {

  public ProductId {
    if (value <= 0) {
      throw new InvalidPriceException("The product identifier must be a positive value.");
    }
  }
}
