package com.inditex.prices.price.domain.model;

import com.inditex.prices.price.domain.exception.InvalidPriceException;

public record BrandId(long value) {

  public BrandId {
    if (value <= 0) {
      throw new InvalidPriceException("The brand identifier must be a positive value.");
    }
  }
}
