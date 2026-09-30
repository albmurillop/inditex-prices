package com.inditex.prices.price.infrastructure.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.inditex.prices.price.domain.model.Price;
import com.inditex.prices.price.domain.model.PriceMother;
import com.inditex.prices.price.domain.repository.PriceRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PriceControllerTest {

  private static final LocalDateTime APPLICATION_DATE = LocalDateTime.of(2020, 6, 14, 10, 0);

  @Autowired private MockMvc mockMvc;

  @MockitoBean private PriceRepository priceRepository;

  @Test
  void shouldReturnApplicablePriceWhenRequestIsValid() throws Exception {
    final Price price = PriceMother.price();
    when(this.priceRepository.findApplicable(price.brandId(), price.productId(), APPLICATION_DATE))
        .thenReturn(Optional.of(price));

    this.mockMvc
        .perform(
            get("/prices")
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", "35455")
                .param("brandId", "1")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productId").value(35455))
        .andExpect(jsonPath("$.brandId").value(1))
        .andExpect(jsonPath("$.priceList").value(1))
        .andExpect(jsonPath("$.startDate").value("2020-06-14T00:00:00"))
        .andExpect(jsonPath("$.endDate").value("2020-12-31T23:59:59"))
        .andExpect(jsonPath("$.price").value(35.50))
        .andExpect(jsonPath("$.currency").value("EUR"));
  }

  @Test
  void shouldReturnNotFoundWhenNoPriceApplies() throws Exception {
    final Price price = PriceMother.price();
    when(this.priceRepository.findApplicable(price.brandId(), price.productId(), APPLICATION_DATE))
        .thenReturn(Optional.empty());

    this.mockMvc
        .perform(
            get("/prices")
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.path").value("/prices"))
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void shouldReturnBadRequestWhenDateHasNoTimeComponent() throws Exception {
    this.mockMvc
        .perform(
            get("/prices")
                .param("applicationDate", "2020-06-14")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestWhenDateUsesSlashFormat() throws Exception {
    this.mockMvc
        .perform(
            get("/prices")
                .param("applicationDate", "14/06/2020 10:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestWhenParameterIsMissing() throws Exception {
    this.mockMvc
        .perform(get("/prices").param("productId", "35455").param("brandId", "1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestWhenIdIsNotPositive() throws Exception {
    this.mockMvc
        .perform(
            get("/prices")
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", "0")
                .param("brandId", "1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnInternalServerErrorWhenAnUnexpectedFailureOccurs() throws Exception {
    final Price price = PriceMother.price();
    when(this.priceRepository.findApplicable(price.brandId(), price.productId(), APPLICATION_DATE))
        .thenThrow(new IllegalStateException("Unexpected failure reading the database"));

    this.mockMvc
        .perform(
            get("/prices")
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.status").value(500))
        .andExpect(jsonPath("$.message").value("An unexpected error has occurred."))
        .andExpect(jsonPath("$.path").value("/prices"));
  }

  @Test
  void shouldReturnClientErrorWhenMethodIsNotAllowed() throws Exception {
    this.mockMvc
        .perform(
            post("/prices")
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(result -> assertThat(result.getResponse().getStatus()).isIn(401, 403));
  }
}
