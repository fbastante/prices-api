package com.inditex.prices.adapters.outbound.database.productprices.converter;

import com.inditex.prices.BasePricesAPIApplicationTest;
import com.inditex.prices.domain.exception.ConversionException;
import com.inditex.prices.domain.model.ProductPrice;
import com.inditex.prices.infrastructure.adapters.outbound.database.productprices.model.ProductPriceJPAEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.MappingException;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class ProductPriceJPAEntityToProductPriceConverterTest extends BasePricesAPIApplicationTest {

    @BeforeEach
    void setup() {
        log.debug("setup()");
    }


    @Test
    void testCurrencyCodeNull() {
        log.debug("testCurrencyCodeNull - building expected result and source objects");
        final ZonedDateTime startDate = ZonedDateTime.now();
        final ZonedDateTime endDate = ZonedDateTime.now().plusDays(5);

        final ProductPrice expectedResult = ProductPrice.builder()
                .brandId(1)
                .productId(35455)
                .startDate(startDate)
                .endDate(endDate)
                .priceList(3)
                .price(65.75)
                .priority(2)
                .currency(null)
                .build();

        final ProductPriceJPAEntity sourceEntity = ProductPriceJPAEntity.builder()
                .brandId(1)
                .productId(35455)
                .startDate(startDate)
                .endDate(endDate)
                .priceList(3)
                .price(65.75)
                .priority(2)
                .currencyCode(null)
                .build();

        final ProductPrice result = modelMapper.map(sourceEntity, ProductPrice.class);
        log.debug("testCurrencyCodeNull - mapped result: {}", result);
        assertEqualsRecursively(result, expectedResult);
    }

    @Test
    void testCurrencyCodeNotParseable() {
        log.debug("testCurrencyCodeNotParseable - building expected result and source objects");
        final ZonedDateTime startDate = ZonedDateTime.now();
        final ZonedDateTime endDate = ZonedDateTime.now().plusDays(5);

        final String invalidCurrencyCode = "notvalid";

        final ConversionException conversionException = new ConversionException(ProductPriceJPAEntity.class,
                ProductPrice.class, String.format("Error parsing currencyCode, found '%s'", invalidCurrencyCode));

        final ProductPriceJPAEntity sourceEntity = ProductPriceJPAEntity.builder()
                .brandId(1)
                .productId(35455)
                .startDate(startDate)
                .endDate(endDate)
                .priceList(3)
                .price(65.75)
                .priority(2)
                .currencyCode(invalidCurrencyCode)
                .build();

        final MappingException thrownException = assertThrows(MappingException.class,
                () -> modelMapper.map(sourceEntity, ProductPrice.class));

        log.debug("testCurrencyCodeNotParseable - thrown exception: {}", thrownException.getClass().getSimpleName());
        assertNotNull(thrownException.getCause());
        assertInstanceOf(ConversionException.class, thrownException.getCause());
        assertEqualsRecursively(thrownException.getCause(), conversionException);
    }

}