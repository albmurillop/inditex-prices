package com.inditex.prices.price.application.service;

import com.inditex.prices.price.application.mapper.ApplicablePriceMapper;
import com.inditex.prices.price.application.port.in.FindApplicablePriceUseCase;
import com.inditex.prices.price.application.query.FindApplicablePriceQuery;
import com.inditex.prices.price.application.result.ApplicablePrice;
import com.inditex.prices.price.domain.model.BrandId;
import com.inditex.prices.price.domain.model.Price;
import com.inditex.prices.price.domain.model.ProductId;
import com.inditex.prices.price.domain.service.ApplicablePriceFinder;

public class FindApplicablePriceService implements FindApplicablePriceUseCase {

  private final ApplicablePriceFinder applicablePriceFinder;
  private final ApplicablePriceMapper applicablePriceMapper;

  public FindApplicablePriceService(
      final ApplicablePriceFinder applicablePriceFinder,
      final ApplicablePriceMapper applicablePriceMapper) {
    this.applicablePriceFinder = applicablePriceFinder;
    this.applicablePriceMapper = applicablePriceMapper;
  }

  @Override
  public ApplicablePrice findApplicablePrice(final FindApplicablePriceQuery query) {
    final Price price =
        this.applicablePriceFinder.find(
            new BrandId(query.brandId()),
            new ProductId(query.productId()),
            query.applicationDate());
    return this.applicablePriceMapper.toResult(price);
  }
}
