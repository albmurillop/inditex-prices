package com.inditex.prices.price.domain.factory;

import com.inditex.prices.price.domain.model.ApplicationPeriod;
import com.inditex.prices.price.domain.model.BrandId;
import com.inditex.prices.price.domain.model.Money;
import com.inditex.prices.price.domain.model.Price;
import com.inditex.prices.price.domain.model.PriceListId;
import com.inditex.prices.price.domain.model.Priority;
import com.inditex.prices.price.domain.model.ProductId;

public final class PriceFactory {

  private PriceFactory() {}

  public static Price create(
      final BrandId brandId,
      final ProductId productId,
      final PriceListId priceListId,
      final ApplicationPeriod period,
      final Priority priority,
      final Money money) {
    return new Price(brandId, productId, priceListId, period, priority, money);
  }
}
