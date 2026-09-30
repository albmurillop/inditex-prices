package com.inditex.prices.price.domain.model;

import com.inditex.prices.price.domain.exception.InvalidPriceException;
import java.util.Objects;

public final class Price {

  private final BrandId brandId;
  private final ProductId productId;
  private final PriceListId priceListId;
  private final ApplicationPeriod period;
  private final Priority priority;
  private final Money money;

  public Price(
      final BrandId brandId,
      final ProductId productId,
      final PriceListId priceListId,
      final ApplicationPeriod period,
      final Priority priority,
      final Money money) {
    this.brandId = requireNonNull(brandId, "Brand is required.");
    this.productId = requireNonNull(productId, "Product is required.");
    this.priceListId = requireNonNull(priceListId, "Price list is required.");
    this.period = requireNonNull(period, "Application period is required.");
    this.priority = requireNonNull(priority, "Priority is required.");
    this.money = requireNonNull(money, "Money is required.");
  }

  private static <T> T requireNonNull(final T value, final String message) {
    if (value == null) {
      throw new InvalidPriceException(message);
    }
    return value;
  }

  public BrandId brandId() {
    return this.brandId;
  }

  public ProductId productId() {
    return this.productId;
  }

  public PriceListId priceListId() {
    return this.priceListId;
  }

  public ApplicationPeriod period() {
    return this.period;
  }

  public Priority priority() {
    return this.priority;
  }

  public Money money() {
    return this.money;
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof final Price price)) {
      return false;
    }
    return this.brandId.equals(price.brandId)
        && this.productId.equals(price.productId)
        && this.priceListId.equals(price.priceListId)
        && this.period.equals(price.period)
        && this.priority.equals(price.priority)
        && this.money.equals(price.money);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.brandId, this.productId, this.priceListId, this.period, this.priority, this.money);
  }
}
