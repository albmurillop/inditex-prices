package com.inditex.prices.price.domain.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inditex.prices.price.domain.exception.InvalidPriceException;
import com.inditex.prices.price.domain.model.ApplicationPeriod;
import com.inditex.prices.price.domain.model.BrandId;
import com.inditex.prices.price.domain.model.Money;
import com.inditex.prices.price.domain.model.Price;
import com.inditex.prices.price.domain.model.PriceListId;
import com.inditex.prices.price.domain.model.Priority;
import com.inditex.prices.price.domain.model.ProductId;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import org.junit.jupiter.api.Test;

class PriceFactoryTest {

  private static final BrandId BRAND_ID = new BrandId(1L);
  private static final ProductId PRODUCT_ID = new ProductId(35455L);
  private static final PriceListId PRICE_LIST_ID = new PriceListId(1L);
  private static final ApplicationPeriod PERIOD =
      new ApplicationPeriod(
          LocalDateTime.of(2020, 6, 14, 0, 0), LocalDateTime.of(2020, 12, 31, 23, 59, 59));
  private static final Priority PRIORITY = new Priority(0);
  private static final Money MONEY =
      new Money(new BigDecimal("35.50"), Currency.getInstance("EUR"));

  @Test
  void shouldCreatePriceWhenInvariantsAreValid() {
    final Price price =
        PriceFactory.create(BRAND_ID, PRODUCT_ID, PRICE_LIST_ID, PERIOD, PRIORITY, MONEY);

    assertThat(price.brandId()).isEqualTo(BRAND_ID);
    assertThat(price.productId()).isEqualTo(PRODUCT_ID);
    assertThat(price.priceListId()).isEqualTo(PRICE_LIST_ID);
    assertThat(price.period()).isEqualTo(PERIOD);
    assertThat(price.priority()).isEqualTo(PRIORITY);
    assertThat(price.money()).isEqualTo(MONEY);
  }

  @Test
  void shouldThrowInvalidPriceExceptionWhenBrandIdIsNull() {
    assertThatThrownBy(
            () -> PriceFactory.create(null, PRODUCT_ID, PRICE_LIST_ID, PERIOD, PRIORITY, MONEY))
        .isInstanceOf(InvalidPriceException.class);
  }

  @Test
  void shouldThrowInvalidPriceExceptionWhenMoneyIsNull() {
    assertThatThrownBy(
            () -> PriceFactory.create(BRAND_ID, PRODUCT_ID, PRICE_LIST_ID, PERIOD, PRIORITY, null))
        .isInstanceOf(InvalidPriceException.class);
  }
}
