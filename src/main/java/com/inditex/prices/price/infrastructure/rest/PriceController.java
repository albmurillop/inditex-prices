package com.inditex.prices.price.infrastructure.rest;

import com.inditex.prices.price.application.port.in.FindApplicablePriceUseCase;
import com.inditex.prices.price.application.query.FindApplicablePriceQuery;
import com.inditex.prices.price.application.result.ApplicablePrice;
import com.inditex.prices.price.infrastructure.rest.api.PricesApi;
import com.inditex.prices.price.infrastructure.rest.dto.PriceResponse;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PriceController implements PricesApi {

  private final FindApplicablePriceUseCase findApplicablePriceUseCase;
  private final PriceRestMapper priceRestMapper;

  public PriceController(
      final FindApplicablePriceUseCase findApplicablePriceUseCase,
      final PriceRestMapper priceRestMapper) {
    this.findApplicablePriceUseCase = findApplicablePriceUseCase;
    this.priceRestMapper = priceRestMapper;
  }

  @Override
  public ResponseEntity<PriceResponse> getApplicablePrice(
      final LocalDateTime applicationDate, final Long productId, final Long brandId) {
    final ApplicablePrice applicablePrice =
        this.findApplicablePriceUseCase.findApplicablePrice(
            new FindApplicablePriceQuery(brandId, productId, applicationDate));
    return ResponseEntity.ok(this.priceRestMapper.toResponse(applicablePrice));
  }
}
