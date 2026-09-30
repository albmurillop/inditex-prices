package com.inditex.prices.price.infrastructure.persistence;

import com.inditex.prices.price.domain.factory.PriceFactory;
import com.inditex.prices.price.domain.model.ApplicationPeriod;
import com.inditex.prices.price.domain.model.BrandId;
import com.inditex.prices.price.domain.model.Money;
import com.inditex.prices.price.domain.model.Price;
import com.inditex.prices.price.domain.model.PriceListId;
import com.inditex.prices.price.domain.model.Priority;
import com.inditex.prices.price.domain.model.ProductId;
import java.util.Currency;
import org.springframework.stereotype.Component;

@Component
public class PricePersistenceMapper {

  public Price toDomain(final PriceEntity entity) {
    return PriceFactory.create(
        new BrandId(entity.getBrandId()),
        new ProductId(entity.getProductId()),
        new PriceListId(entity.getPriceList()),
        new ApplicationPeriod(entity.getStartDate(), entity.getEndDate()),
        new Priority(entity.getPriority()),
        new Money(entity.getPrice(), Currency.getInstance(entity.getCurr())));
  }
}
