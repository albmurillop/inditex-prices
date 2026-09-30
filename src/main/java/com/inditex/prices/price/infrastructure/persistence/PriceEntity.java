package com.inditex.prices.price.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRICES")
public class PriceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "BRAND_ID", nullable = false)
  private Long brandId;

  @Column(name = "START_DATE", nullable = false)
  private LocalDateTime startDate;

  @Column(name = "END_DATE", nullable = false)
  private LocalDateTime endDate;

  @Column(name = "PRICE_LIST", nullable = false)
  private Long priceList;

  @Column(name = "PRODUCT_ID", nullable = false)
  private Long productId;

  @Column(name = "PRIORITY", nullable = false)
  private Integer priority;

  @Column(name = "PRICE", nullable = false)
  private BigDecimal price;

  @Column(name = "CURR", nullable = false, columnDefinition = "CHAR(3)")
  private String curr;

  protected PriceEntity() {}

  public Long getId() {
    return this.id;
  }

  public Long getBrandId() {
    return this.brandId;
  }

  public LocalDateTime getStartDate() {
    return this.startDate;
  }

  public LocalDateTime getEndDate() {
    return this.endDate;
  }

  public Long getPriceList() {
    return this.priceList;
  }

  public Long getProductId() {
    return this.productId;
  }

  public Integer getPriority() {
    return this.priority;
  }

  public BigDecimal getPrice() {
    return this.price;
  }

  public String getCurr() {
    return this.curr;
  }
}
