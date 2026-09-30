package com.inditex.prices.price.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.inditex.prices.price.domain.exception.PriceNotFoundException;
import com.inditex.prices.price.domain.factory.PriceFactory;
import com.inditex.prices.price.domain.model.ApplicationPeriod;
import com.inditex.prices.price.domain.model.BrandId;
import com.inditex.prices.price.domain.model.Money;
import com.inditex.prices.price.domain.model.Price;
import com.inditex.prices.price.domain.model.PriceListId;
import com.inditex.prices.price.domain.model.Priority;
import com.inditex.prices.price.domain.model.ProductId;
import com.inditex.prices.price.domain.repository.PriceRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApplicablePriceFinderTest {

  private final BrandId brandId = new BrandId(1L);
  private final ProductId productId = new ProductId(35455L);
  private final LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
  @Mock private PriceRepository priceRepository;

  @Test
  void shouldReturnPriceWhenRepositoryFindsOne() {
    final Price expectedPrice =
        PriceFactory.create(
            this.brandId,
            this.productId,
            new PriceListId(1L),
            new ApplicationPeriod(
                this.applicationDate.minusHours(1), this.applicationDate.plusHours(1)),
            new Priority(0),
            new Money(new BigDecimal("35.50"), Currency.getInstance("EUR")));
    when(this.priceRepository.findApplicable(this.brandId, this.productId, this.applicationDate))
        .thenReturn(Optional.of(expectedPrice));

    final ApplicablePriceFinder finder = new ApplicablePriceFinder(this.priceRepository);

    assertThat(finder.find(this.brandId, this.productId, this.applicationDate))
        .isEqualTo(expectedPrice);
  }

  @Test
  void shouldThrowPriceNotFoundExceptionWhenRepositoryFindsNothing() {
    when(this.priceRepository.findApplicable(this.brandId, this.productId, this.applicationDate))
        .thenReturn(Optional.empty());

    final ApplicablePriceFinder finder = new ApplicablePriceFinder(this.priceRepository);

    assertThatThrownBy(() -> finder.find(this.brandId, this.productId, this.applicationDate))
        .isInstanceOf(PriceNotFoundException.class);
  }
}
