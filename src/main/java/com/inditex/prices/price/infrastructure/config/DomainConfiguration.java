package com.inditex.prices.price.infrastructure.config;

import com.inditex.prices.price.application.mapper.ApplicablePriceMapper;
import com.inditex.prices.price.application.port.in.FindApplicablePriceUseCase;
import com.inditex.prices.price.application.service.FindApplicablePriceService;
import com.inditex.prices.price.domain.repository.PriceRepository;
import com.inditex.prices.price.domain.service.ApplicablePriceFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

  @Bean
  public ApplicablePriceFinder applicablePriceFinder(final PriceRepository priceRepository) {
    return new ApplicablePriceFinder(priceRepository);
  }

  @Bean
  public ApplicablePriceMapper applicablePriceMapper() {
    return new ApplicablePriceMapper();
  }

  @Bean
  public FindApplicablePriceUseCase findApplicablePriceUseCase(
      final ApplicablePriceFinder applicablePriceFinder,
      final ApplicablePriceMapper applicablePriceMapper) {
    return new FindApplicablePriceService(applicablePriceFinder, applicablePriceMapper);
  }
}
