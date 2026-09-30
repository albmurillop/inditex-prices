package com.inditex.prices.price.domain.service;

import com.inditex.prices.price.domain.exception.PriceNotFoundException;
import com.inditex.prices.price.domain.model.BrandId;
import com.inditex.prices.price.domain.model.Price;
import com.inditex.prices.price.domain.model.ProductId;
import com.inditex.prices.price.domain.repository.PriceRepository;
import java.time.LocalDateTime;

public class ApplicablePriceFinder {

  private final PriceRepository priceRepository;

  public ApplicablePriceFinder(final PriceRepository priceRepository) {
    this.priceRepository = priceRepository;
  }

  public Price find(
      final BrandId brandId, final ProductId productId, final LocalDateTime applicationDate) {
    return this.priceRepository
        .findApplicable(brandId, productId, applicationDate)
        .orElseThrow(
            () ->
                new PriceNotFoundException(
                    "No applicable price found for brand %d, product %d on date %s."
                        .formatted(brandId.value(), productId.value(), applicationDate)));
  }
}
