package com.inditex.prices.infrastructure.adapters.outbound.database.productprices.converter;

import com.inditex.prices.domain.exception.ConversionException;
import com.inditex.prices.domain.model.ProductPrice;
import com.inditex.prices.infrastructure.adapters.outbound.database.productprices.model.ProductPriceJPAEntity;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Currency;
import java.util.Optional;

@NoArgsConstructor
public class ProductPriceJPAEntityToProductPriceConverter implements Converter<ProductPriceJPAEntity, ProductPrice> {

    @Override
    public ProductPrice convert(final MappingContext<ProductPriceJPAEntity, ProductPrice> mappingContext) {
        final ProductPriceJPAEntity source = mappingContext.getSource();
        final ProductPrice destination = mappingContext.getDestination();

        destination.setCurrency(parseCurrencyCode(source.getCurrencyCode()));

        return destination;
    }

    private Currency parseCurrencyCode(final String currencyCode) {
        try {
            return Optional.ofNullable(currencyCode)
                    .map(Currency::getInstance)
                    .orElse(null);
        } catch (IllegalArgumentException e) {
            throw new ConversionException(ProductPriceJPAEntity.class, ProductPrice.class,
                    String.format("Error parsing currencyCode, found '%s'", currencyCode));
        }
    }
}
