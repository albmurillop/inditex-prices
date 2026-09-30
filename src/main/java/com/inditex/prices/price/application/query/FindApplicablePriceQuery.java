package com.inditex.prices.price.application.query;

import java.time.LocalDateTime;

public record FindApplicablePriceQuery(
    long brandId, long productId, LocalDateTime applicationDate) {}
