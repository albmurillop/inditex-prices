package com.inditex.prices.price.application.mapper;

import com.inditex.prices.price.application.result.ApplicablePrice;
import com.inditex.prices.price.domain.model.Price;

public class ApplicablePriceMapper {

  public ApplicablePrice toResult(final Price price) {
    return new ApplicablePrice(
        price.productId().value(),
        price.brandId().value(),
        price.priceListId().value(),
        price.period().start(),
        price.period().end(),
        price.money().amount(),
        price.money().currency().getCurrencyCode());
  }
}
