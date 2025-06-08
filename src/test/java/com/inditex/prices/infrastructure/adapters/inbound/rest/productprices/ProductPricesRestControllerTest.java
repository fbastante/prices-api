package com.inditex.prices.infrastructure.adapters.inbound.rest.productprices;

import com.inditex.prices.BasePricesAPIApplicationTest;
import com.inditex.prices.infrastructure.adapters.inbound.rest.exception.BaseErrorResponse;
import com.inditex.prices.infrastructure.adapters.inbound.rest.productprices.model.GetProductPriceRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class ProductPricesRestControllerTest extends BasePricesAPIApplicationTest {


    private static final String GET_PATH = "/api/v1/product-prices";

    @BeforeEach
    void setup() {
        log.debug("setup()");
    }


    @ParameterizedTest(name = "{0}")
    @MethodSource("listTestSuccessFilterCases")
    void testSuccessFilter(final Integer brandId, final Integer productId, final ZonedDateTime date,
                            final GetProductPriceRestResponse expectedResponse
    ) throws Exception {
        log.debug("testSuccessFilter - calling GET product-prices with params {} - {} - {}", brandId, productId, date);
        String response = mockMvc.perform(MockMvcRequestBuilders.get(GET_PATH)
                        .param("brandId", brandId.toString())
                        .param("productId", productId.toString())
                        .param("date", date.toString())
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        log.debug("testSuccessFilter - received GET response: {}", response);
        GetProductPriceRestResponse responseObj = objectMapper.readValue(response, GetProductPriceRestResponse.class);

        log.debug("testSuccessFilter - mapped GetProductPriceRestResponse object: {}", responseObj);
        assertEqualsRecursively(responseObj, expectedResponse);
    }

    static Stream<Arguments> listTestSuccessFilterCases() {
        return Stream.of(
                Arguments.of(Named.of("Test 1", 1), 35455, ZonedDateTime.parse("2020-06-14T10:00:00Z"),
                        GetProductPriceRestResponse.builder()
                                .brandId(1)
                                .productId(35455)
                                .startDate(ZonedDateTime.parse("2020-06-14T00:00:00Z"))
                                .endDate(ZonedDateTime.parse("2020-12-31T23:59:59Z"))
                                .priceList(1)
                                .price(35.50)
                                .currency("EUR")
                                .build()
                ),
                Arguments.of(Named.of("Test 2", 1), 35455, ZonedDateTime.parse("2020-06-14T16:00:00Z"),
                        GetProductPriceRestResponse.builder()
                                .brandId(1)
                                .productId(35455)
                                .startDate(ZonedDateTime.parse("2020-06-14T15:00:00Z"))
                                .endDate(ZonedDateTime.parse("2020-06-14T18:30:00Z"))
                                .priceList(2)
                                .price(25.45)
                                .currency("EUR")
                                .build()
                ),
                Arguments.of(Named.of("Test 3", 1), 35455, ZonedDateTime.parse("2020-06-14T21:00:00Z"),
                        GetProductPriceRestResponse.builder()
                                .brandId(1)
                                .productId(35455)
                                .startDate(ZonedDateTime.parse("2020-06-14T00:00:00Z"))
                                .endDate(ZonedDateTime.parse("2020-12-31T23:59:59Z"))
                                .priceList(1)
                                .price(35.50)
                                .currency("EUR")
                                .build()
                ),
                Arguments.of(Named.of("Test 4", 1), 35455, ZonedDateTime.parse("2020-06-15T10:00:00Z"),
                        GetProductPriceRestResponse.builder()
                                .brandId(1)
                                .productId(35455)
                                .startDate(ZonedDateTime.parse("2020-06-15T00:00:00Z"))
                                .endDate(ZonedDateTime.parse("2020-06-15T11:00:00Z"))
                                .priceList(3)
                                .price(30.50)
                                .currency("EUR")
                                .build()
                ),
                Arguments.of(Named.of("Test 5", 1), 35455, ZonedDateTime.parse("2020-06-16T21:00:00Z"),
                        GetProductPriceRestResponse.builder()
                                .brandId(1)
                                .productId(35455)
                                .startDate(ZonedDateTime.parse("2020-06-15T16:00:00Z"))
                                .endDate(ZonedDateTime.parse("2020-12-31T23:59:59Z"))
                                .priceList(4)
                                .price(38.95)
                                .currency("EUR")
                                .build()
                ),
                Arguments.of(Named.of("Test 1 Brand 2 ", 2), 5040, ZonedDateTime.parse("2020-06-16T21:00:00Z"),
                        GetProductPriceRestResponse.builder()
                                .brandId(2)
                                .productId(5040)
                                .startDate(ZonedDateTime.parse("2020-06-14T00:00:00Z"))
                                .endDate(ZonedDateTime.parse("2020-12-31T23:59:59Z"))
                                .priceList(10)
                                .price(60.0)
                                .currency("USD")
                                .build()
                ),
                Arguments.of(Named.of("Test 2 Brand 2 ", 2), 5050, ZonedDateTime.parse("2020-06-14T16:00:00Z"),
                        GetProductPriceRestResponse.builder()
                                .brandId(2)
                                .productId(5050)
                                .startDate(ZonedDateTime.parse("2020-06-14T12:00:00Z"))
                                .endDate(ZonedDateTime.parse("2020-06-15T23:59:59Z"))
                                .priceList(11)
                                .price(72.25)
                                .currency("USD")
                                .build()
                )
        );
    }


    @ParameterizedTest(name = "{0}")
    @MethodSource("listNotFoundCases")
    void testNotFound(final Integer brandId, final Integer productId, final ZonedDateTime date) throws Exception {
        log.debug("testNotFound - calling GET product-prices with params {} - {} - {}", brandId, productId, date);

        final ZonedDateTime startTimestamp = ZonedDateTime.now();
        final BaseErrorResponse expectedError = BaseErrorResponse.builder()
                .timestamp(ZonedDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .path(GET_PATH)
                .error(String.format("Element ProductPrice wasn't found for values %s, %s, %s",
                                brandId, productId, date))
                .build();

        String response = mockMvc.perform(MockMvcRequestBuilders.get(GET_PATH)
                        .param("brandId", brandId.toString())
                        .param("productId", productId.toString())
                        .param("date", date.toString())
                ).andReturn().getResponse().getContentAsString();

        ZonedDateTime endTimestamp = ZonedDateTime.now();

        log.debug("testNotFound - received response: {}", response);
        BaseErrorResponse responseObj = objectMapper.readValue(response, BaseErrorResponse.class);

        log.debug("testNotFound - mapped BaseErrorResponse object: {}", responseObj);
        assertEqualsRecursively(responseObj, expectedError, "timestamp");
        assertNotNull(responseObj.getTimestamp());
        assertTrue(startTimestamp.isBefore(responseObj.getTimestamp()));
        assertTrue(endTimestamp.isAfter(responseObj.getTimestamp()));

    }

    static Stream<Arguments> listNotFoundCases() {
        return Stream.of(
                Arguments.of(
                        Named.of("Not found for brandId value",4),
                        35455,
                        ZonedDateTime.parse("2020-06-14T10:00:00Z")
                ),
                Arguments.of(
                        Named.of("Not found for productId value",1),
                        444,
                        ZonedDateTime.parse("2020-06-14T10:00:00Z")
                ),
                Arguments.of(
                        Named.of("Not found for date value",1),
                        35455,
                        ZonedDateTime.parse("2024-06-14T10:00:00Z")
                )
        );
    }
}