package com.inditex.prices.price.infrastructure.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PriceControllerTestIT {

  @Autowired private MockMvc mockMvc;

  @ParameterizedTest(name = "case {0}: {1} -> price list {2} at {3} EUR")
  @CsvSource({
    "1, 2020-06-14T10:00:00, 1, 35.50, 2020-06-14T00:00:00, 2020-12-31T23:59:59",
    "2, 2020-06-14T16:00:00, 2, 25.45, 2020-06-14T15:00:00, 2020-06-14T18:30:00",
    "3, 2020-06-14T21:00:00, 1, 35.50, 2020-06-14T00:00:00, 2020-12-31T23:59:59",
    "4, 2020-06-15T10:00:00, 3, 30.50, 2020-06-15T00:00:00, 2020-06-15T11:00:00",
    "5, 2020-06-16T21:00:00, 4, 38.95, 2020-06-15T16:00:00, 2020-12-31T23:59:59"
  })
  void shouldReturnApplicableTariffWhenGivenEachScenarioOfTheStatement(
      final int testCase,
      final String applicationDate,
      final long priceList,
      final String price,
      final String startDate,
      final String endDate)
      throws Exception {
    this.mockMvc
        .perform(
            get("/prices")
                .param("applicationDate", applicationDate)
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.productId").value(35455))
        .andExpect(jsonPath("$.brandId").value(1))
        .andExpect(jsonPath("$.priceList").value(priceList))
        .andExpect(jsonPath("$.price").value(Double.parseDouble(price)))
        .andExpect(jsonPath("$.currency").value("EUR"))
        .andExpect(jsonPath("$.startDate").value(startDate))
        .andExpect(jsonPath("$.endDate").value(endDate));
  }
}
