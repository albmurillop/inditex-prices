package com.inditex.prices.price.domain.model;

import com.inditex.prices.price.domain.exception.InvalidPriceException;

public record PriceListId(long value) {

  public PriceListId {
    if (value <= 0) {
      throw new InvalidPriceException("The price list identifier must be a positive value.");
    }
  }
}
