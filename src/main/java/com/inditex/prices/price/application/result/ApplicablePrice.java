package com.inditex.prices.price.application.result;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ApplicablePrice(
    long productId,
    long brandId,
    long priceListId,
    LocalDateTime startDate,
    LocalDateTime endDate,
    BigDecimal price,
    String currency) {}
