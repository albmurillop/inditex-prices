package com.inditex.prices.price.application.port.in;

import com.inditex.prices.price.application.query.FindApplicablePriceQuery;
import com.inditex.prices.price.application.result.ApplicablePrice;

public interface FindApplicablePriceUseCase {

  ApplicablePrice findApplicablePrice(FindApplicablePriceQuery query);
}
