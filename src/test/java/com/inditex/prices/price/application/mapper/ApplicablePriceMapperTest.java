package com.inditex.prices.price.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.inditex.prices.price.application.result.ApplicablePrice;
import com.inditex.prices.price.domain.model.PriceMother;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ApplicablePriceMapperTest {

  @Test
  void shouldMapAllFieldsWhenMappingFromDomainPrice() {
    final ApplicablePrice result = new ApplicablePriceMapper().toResult(PriceMother.price());

    assertThat(result.productId()).isEqualTo(35455L);
    assertThat(result.brandId()).isEqualTo(1L);
    assertThat(result.priceListId()).isEqualTo(1L);
    assertThat(result.startDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 0, 0));
    assertThat(result.endDate()).isEqualTo(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
    assertThat(result.price()).isEqualByComparingTo("35.50");
    assertThat(result.currency()).isEqualTo("EUR");
  }
}
