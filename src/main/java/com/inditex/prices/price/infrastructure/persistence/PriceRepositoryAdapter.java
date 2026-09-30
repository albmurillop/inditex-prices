package com.inditex.prices.price.infrastructure.persistence;

import com.inditex.prices.price.domain.model.BrandId;
import com.inditex.prices.price.domain.model.Price;
import com.inditex.prices.price.domain.model.ProductId;
import com.inditex.prices.price.domain.repository.PriceRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class PriceRepositoryAdapter implements PriceRepository {

  private final PriceJpaRepository priceJpaRepository;
  private final PricePersistenceMapper priceMapper;

  public PriceRepositoryAdapter(
      final PriceJpaRepository priceJpaRepository, final PricePersistenceMapper priceMapper) {
    this.priceJpaRepository = priceJpaRepository;
    this.priceMapper = priceMapper;
  }

  @Override
  public Optional<Price> findApplicable(
      final BrandId brandId, final ProductId productId, final LocalDateTime applicationDate) {
    return this.priceJpaRepository
        .findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDescStartDateDesc(
            brandId.value(), productId.value(), applicationDate, applicationDate)
        .map(this.priceMapper::toDomain);
  }
}
