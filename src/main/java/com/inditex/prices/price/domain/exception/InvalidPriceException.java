package com.inditex.prices.price.domain.exception;

import com.inditex.prices.shared.domain.DomainException;

public class InvalidPriceException extends DomainException {

  public InvalidPriceException(final String message) {
    super(message);
  }
}
