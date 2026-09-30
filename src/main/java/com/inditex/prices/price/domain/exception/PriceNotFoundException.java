package com.inditex.prices.price.domain.exception;

import com.inditex.prices.shared.domain.DomainException;

public class PriceNotFoundException extends DomainException {

  public PriceNotFoundException(final String message) {
    super(message);
  }
}
