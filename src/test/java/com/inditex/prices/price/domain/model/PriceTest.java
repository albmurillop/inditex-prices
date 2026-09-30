package com.inditex.prices.price.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.inditex.prices.price.domain.factory.PriceFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import org.junit.jupiter.api.Test;

class PriceTest {

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
  void shouldBeEqualWhenAllFieldsMatch() {
    final Price price =
        PriceFactory.create(BRAND_ID, PRODUCT_ID, PRICE_LIST_ID, PERIOD, PRIORITY, MONEY);
    final Price samePrice =
        PriceFactory.create(BRAND_ID, PRODUCT_ID, PRICE_LIST_ID, PERIOD, PRIORITY, MONEY);

    assertThat(price).isEqualTo(samePrice);
    assertThat(price.hashCode()).isEqualTo(samePrice.hashCode());
  }

  @Test
  void shouldNotBeEqualWhenPriceListDiffers() {
    final Price price =
        PriceFactory.create(BRAND_ID, PRODUCT_ID, PRICE_LIST_ID, PERIOD, PRIORITY, MONEY);
    final Price otherPriceList =
        PriceFactory.create(BRAND_ID, PRODUCT_ID, new PriceListId(2L), PERIOD, PRIORITY, MONEY);

    assertThat(price).isNotEqualTo(otherPriceList);
  }

  @Test
  void shouldNotBeEqualWhenPriorityDiffers() {
    final Price price =
        PriceFactory.create(BRAND_ID, PRODUCT_ID, PRICE_LIST_ID, PERIOD, PRIORITY, MONEY);
    final Price otherPriority =
        PriceFactory.create(BRAND_ID, PRODUCT_ID, PRICE_LIST_ID, PERIOD, new Priority(1), MONEY);

    assertThat(price).isNotEqualTo(otherPriority);
  }

  @Test
  void shouldNotBeEqualWhenMoneyDiffers() {
    final Price price =
        PriceFactory.create(BRAND_ID, PRODUCT_ID, PRICE_LIST_ID, PERIOD, PRIORITY, MONEY);
    final Price otherMoney =
        PriceFactory.create(
            BRAND_ID,
            PRODUCT_ID,
            PRICE_LIST_ID,
            PERIOD,
            PRIORITY,
            new Money(new BigDecimal("40.00"), Currency.getInstance("EUR")));

    assertThat(price).isNotEqualTo(otherMoney);
  }

  @Test
  void shouldNotBeEqualWhenPeriodDiffers() {
    final Price price =
        PriceFactory.create(BRAND_ID, PRODUCT_ID, PRICE_LIST_ID, PERIOD, PRIORITY, MONEY);
    final Price otherPeriod =
        PriceFactory.create(
            BRAND_ID,
            PRODUCT_ID,
            PRICE_LIST_ID,
            new ApplicationPeriod(PERIOD.start(), PERIOD.end().minusDays(1)),
            PRIORITY,
            MONEY);

    assertThat(price).isNotEqualTo(otherPeriod);
  }
}
