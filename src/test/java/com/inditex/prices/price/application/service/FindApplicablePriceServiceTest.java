package com.inditex.prices.price.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.inditex.prices.price.application.mapper.ApplicablePriceMapper;
import com.inditex.prices.price.application.query.FindApplicablePriceQuery;
import com.inditex.prices.price.application.result.ApplicablePrice;
import com.inditex.prices.price.domain.model.Price;
import com.inditex.prices.price.domain.model.PriceMother;
import com.inditex.prices.price.domain.service.ApplicablePriceFinder;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindApplicablePriceServiceTest {

  private static final LocalDateTime APPLICATION_DATE = LocalDateTime.of(2020, 6, 14, 10, 0);

  @Mock private ApplicablePriceFinder applicablePriceFinder;

  @Test
  void shouldMapDomainPriceIntoApplicablePriceWhenPriceIsFound() {
    final Price price = PriceMother.price();
    when(this.applicablePriceFinder.find(price.brandId(), price.productId(), APPLICATION_DATE))
        .thenReturn(price);

    final FindApplicablePriceService service =
        new FindApplicablePriceService(this.applicablePriceFinder, new ApplicablePriceMapper());
    final ApplicablePrice result =
        service.findApplicablePrice(
            new FindApplicablePriceQuery(
                price.brandId().value(), price.productId().value(), APPLICATION_DATE));

    assertThat(result.brandId()).isEqualTo(1L);
    assertThat(result.productId()).isEqualTo(35455L);
    assertThat(result.priceListId()).isEqualTo(1L);
    assertThat(result.startDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 0, 0));
    assertThat(result.endDate()).isEqualTo(LocalDateTime.of(2020, 12, 31, 23, 59, 59));
    assertThat(result.price()).isEqualByComparingTo("35.50");
    assertThat(result.currency()).isEqualTo("EUR");
  }
}
