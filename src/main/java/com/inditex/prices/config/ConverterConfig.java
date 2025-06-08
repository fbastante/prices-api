package com.inditex.prices.config;

import com.inditex.prices.domain.model.ProductPrice;
import com.inditex.prices.infrastructure.adapters.outbound.database.productprices.converter.ProductPriceJPAEntityToProductPriceConverter;
import com.inditex.prices.infrastructure.adapters.outbound.database.productprices.model.ProductPriceJPAEntity;
import jakarta.annotation.Resource;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterConfig {

    @Resource
    private ModelMapper modelMapper;

    @Bean
    public ProductPriceJPAEntityToProductPriceConverter productPriceJPAEntityToProductPriceConverter() {
        ProductPriceJPAEntityToProductPriceConverter converter = new ProductPriceJPAEntityToProductPriceConverter();
        modelMapper.createTypeMap(ProductPriceJPAEntity.class, ProductPrice.class).setPostConverter(converter);

        return converter;
    }
}
