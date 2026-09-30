package com.inditex.prices.price.infrastructure.rest;

import com.inditex.prices.price.application.result.ApplicablePrice;
import com.inditex.prices.price.infrastructure.rest.dto.PriceResponse;
import org.springframework.stereotype.Component;

@Component
public class PriceRestMapper {

  public PriceResponse toResponse(final ApplicablePrice applicablePrice) {
    return new PriceResponse(
        applicablePrice.productId(),
        applicablePrice.brandId(),
        applicablePrice.priceListId(),
        applicablePrice.startDate(),
        applicablePrice.endDate(),
        applicablePrice.price(),
        applicablePrice.currency());
  }
}
