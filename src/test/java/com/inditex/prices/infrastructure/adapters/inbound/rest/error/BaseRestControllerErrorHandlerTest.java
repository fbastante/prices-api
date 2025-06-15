package com.inditex.prices.infrastructure.adapters.inbound.rest.error;

import com.inditex.prices.BasePricesAPIApplicationTest;
import com.inditex.prices.infrastructure.adapters.inbound.rest.error.model.BaseErrorRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class BaseRestControllerErrorHandlerTest extends BasePricesAPIApplicationTest {

    private static final String GET_PATH = "/api/v1/product-prices";

    @BeforeEach
    void setup() {
        log.debug("setup()");
    }

    @AfterEach
    void clean() {
        log.debug("clean()");
        Mockito.clearInvocations(productPriceJPARepository);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("listErrorCases")
    void testExceptionHandling(final MockHttpServletRequestBuilder requestBuilder, final String expectedPath,
                                         final Integer expectedStatus, final String expectedMessage
                      ) throws Exception {
        log.debug("testExceptionHandling - calling GET product-prices for exception handling test: {} - {}",
                expectedMessage, expectedMessage);

        final ZonedDateTime startTimestamp = ZonedDateTime.now();
        final BaseErrorRestResponse expectedError = BaseErrorRestResponse.builder()
                .timestamp(ZonedDateTime.now())
                .status(expectedStatus)
                .path(expectedPath)
                .message(expectedMessage)
                .build();

        final String response = mockMvc.perform(requestBuilder)
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();

        ZonedDateTime endTimestamp = ZonedDateTime.now();

        log.debug("testExceptionHandling - received response: {}", response);
        BaseErrorRestResponse responseObj = objectMapper.readValue(response, BaseErrorRestResponse.class);

        log.debug("testExceptionHandling - mapped BaseErrorResponse object: {}", responseObj);
        assertEqualsRecursively(responseObj, expectedError, "timestamp");
        assertNotNull(responseObj.getTimestamp());
        assertTrue(startTimestamp.isBefore(responseObj.getTimestamp()));
        assertTrue(endTimestamp.isAfter(responseObj.getTimestamp()));

    }

    static Stream<Arguments> listErrorCases() {
        return Stream.of(
                Arguments.of(
                        Named.of("ValidationException",
                                MockMvcRequestBuilders.get(GET_PATH)
                                        .param("brandId", "1")
                                        .param("productId", "35455")
                        ),
                        GET_PATH,
                        HttpStatus.BAD_REQUEST.value(),
                        "getProductPrice.request.date: must not be null"
                ),
                Arguments.of(
                        Named.of("MethodArgumentNotValidException",
                                MockMvcRequestBuilders.get(GET_PATH).param("brandId", "notvalidbrand")
                        ),
                        GET_PATH,
                        HttpStatus.BAD_REQUEST.value(),
                        "validation error: field 'brandId' with invalid value 'notvalidbrand'"
                ),
                Arguments.of(
                        Named.of("NoResourceFoundException",
                                MockMvcRequestBuilders.get(GET_PATH+"/notexisting")
                        ),
                        GET_PATH + "/notexisting",
                        HttpStatus.NOT_FOUND.value(),
                        "No static resource api/v1/product-prices/notexisting."
                ),
                Arguments.of(
                        Named.of("HttpRequestMethodNotSupportedException",
                                MockMvcRequestBuilders.post(GET_PATH)
                        ),
                        GET_PATH,
                        HttpStatus.METHOD_NOT_ALLOWED.value(),
                        "method 'POST' not supported"
                )
        );
    }

    //this test is here because some exceptions can't be thrown from the product-prices endpoint
    @ParameterizedTest(name = "{0}")
    @MethodSource("listMockedExceptionCases")
    void testMockedExceptionHandling(final Exception thrownException, final String expectedPath,
                             final Integer expectedStatus, final String expectedMessage) throws Exception {
        log.debug("testMockedExceptionHandling - calling GET product-prices with mocked exception in UseCase");

        Mockito.when(productPriceJPARepository.findAppliedProductPriceByBrandAndProductAndDate(any(), any(), any()))
                .thenThrow(thrownException);

        final ZonedDateTime startTimestamp = ZonedDateTime.now();
        final BaseErrorRestResponse expectedError = BaseErrorRestResponse.builder()
                .timestamp(ZonedDateTime.now())
                .status(expectedStatus)
                .path(GET_PATH)
                .message(expectedMessage)
                .build();

        final String response = mockMvc.perform(MockMvcRequestBuilders.get(GET_PATH)
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("date", "2020-12-31T23:59:59Z"))
                .andExpect(status().is(expectedStatus))
                .andReturn().getResponse().getContentAsString();

        ZonedDateTime endTimestamp = ZonedDateTime.now();

        log.debug("testMockedExceptionHandling - received response: {}", response);
        BaseErrorRestResponse responseObj = objectMapper.readValue(response, BaseErrorRestResponse.class);

        log.debug("testMockedExceptionHandling - mapped BaseErrorResponse object: {}", responseObj);
        assertEqualsRecursively(responseObj, expectedError, "timestamp");
        assertNotNull(responseObj.getTimestamp());
        assertTrue(startTimestamp.isBefore(responseObj.getTimestamp()));
        assertTrue(endTimestamp.isAfter(responseObj.getTimestamp()));
    }

    static Stream<Arguments> listMockedExceptionCases() throws NoSuchMethodException {
        return Stream.of(
                Arguments.of(
                        Named.of("HttpMessageNotReadableException",
                                new HttpMessageNotReadableException("error parsing JSON content", Mockito.mock(HttpInputMessage.class))
                        ),
                        GET_PATH,
                        HttpStatus.BAD_REQUEST.value(),
                        "Message is not readable: error parsing JSON content"
                ),
                Arguments.of(
                        Named.of("MaxUploadSizeExceededException",
                                new MaxUploadSizeExceededException(99)
                        ),
                        GET_PATH,
                        HttpStatus.PAYLOAD_TOO_LARGE.value(),
                        "Maximum upload size of 99 bytes exceeded"
                ),
                Arguments.of(
                        Named.of("RuntimeException", new RuntimeException("mocked internal error")),
                        GET_PATH,
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal server error"
                )
        );
    }


    @Test
    //As we are asking the response to be in XML format and the product-prices endpoint can't return a XML response,
    //it will return a 406 with empty body
    void testMediaTypeNotAcceptable() throws Exception {
        log.debug("testMediaTypeNotAcceptable - calling GET product-prices with unsupported 'accept' header");

        mockMvc.perform(MockMvcRequestBuilders.get(GET_PATH)
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("date", "2020-12-31T23:59:59Z")
                        .accept(MediaType.APPLICATION_XML)
                )
                .andExpect(status().isNotAcceptable());
    }
}
