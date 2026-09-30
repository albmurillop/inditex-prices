package com.inditex.prices.price.domain.repository;

import com.inditex.prices.price.domain.model.BrandId;
import com.inditex.prices.price.domain.model.Price;
import com.inditex.prices.price.domain.model.ProductId;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceRepository {

  Optional<Price> findApplicable(
      BrandId brandId, ProductId productId, LocalDateTime applicationDate);
}
