package com.inditex.prices.price.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.inditex.prices.price.domain.model.BrandId;
import com.inditex.prices.price.domain.model.Price;
import com.inditex.prices.price.domain.model.ProductId;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({PriceRepositoryAdapter.class, PricePersistenceMapper.class})
class PriceRepositoryAdapterTest {

  private static final BrandId BRAND_ID = new BrandId(1L);
  private static final ProductId PRODUCT_ID = new ProductId(35455L);

  @Autowired private PriceRepositoryAdapter priceRepositoryAdapter;

  @ParameterizedTest(name = "{0} -> price list {1}")
  @CsvSource({
    "2020-06-14T15:00:00, 2",
    "2020-06-14T18:30:00, 2",
    "2020-06-14T18:30:01, 1",
    "2020-12-31T23:59:59, 4"
  })
  void shouldReturnHighestPriorityPriceWhenDateIsAtTheBoundary(
      final String applicationDate, final long expectedPriceList) {
    final Optional<Price> price =
        this.priceRepositoryAdapter.findApplicable(
            BRAND_ID, PRODUCT_ID, LocalDateTime.parse(applicationDate));

    assertThat(price).isPresent();
    assertThat(price.get().priceListId().value()).isEqualTo(expectedPriceList);
  }

  @Test
  void shouldReturnEmptyWhenDateIsBeforeAnyPriceStarts() {
    final Optional<Price> price =
        this.priceRepositoryAdapter.findApplicable(
            BRAND_ID, PRODUCT_ID, LocalDateTime.parse("2020-06-13T23:59:59"));

    assertThat(price).isEmpty();
  }

  @Test
  void shouldReturnEmptyWhenBrandIsUnknown() {
    final Optional<Price> price =
        this.priceRepositoryAdapter.findApplicable(
            new BrandId(99L), PRODUCT_ID, LocalDateTime.parse("2020-06-14T10:00:00"));

    assertThat(price).isEmpty();
  }

  @Test
  void shouldReturnEmptyWhenProductIsUnknown() {
    final Optional<Price> price =
        this.priceRepositoryAdapter.findApplicable(
            BRAND_ID, new ProductId(99999L), LocalDateTime.parse("2020-06-14T10:00:00"));

    assertThat(price).isEmpty();
  }
}
