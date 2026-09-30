package com.inditex.prices.price.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceJpaRepository extends JpaRepository<PriceEntity, Long> {

  Optional<PriceEntity>
      findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDescStartDateDesc(
          Long brandId, Long productId, LocalDateTime startDate, LocalDateTime endDate);
}
