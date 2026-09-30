package com.inditex.prices.price.domain.model;

import com.inditex.prices.price.domain.factory.PriceFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public final class PriceMother {

  private PriceMother() {}

  public static Price price() {
    return PriceFactory.create(
        new BrandId(1L),
        new ProductId(35455L),
        new PriceListId(1L),
        new ApplicationPeriod(
            LocalDateTime.of(2020, 6, 14, 0, 0), LocalDateTime.of(2020, 12, 31, 23, 59, 59)),
        new Priority(0),
        new Money(new BigDecimal("35.50"), Currency.getInstance("EUR")));
  }
}
