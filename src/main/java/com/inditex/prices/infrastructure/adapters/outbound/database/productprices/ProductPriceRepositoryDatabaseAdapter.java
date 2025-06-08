package com.inditex.prices.infrastructure.adapters.outbound.database.productprices;


import com.inditex.prices.domain.model.ProductPrice;
import com.inditex.prices.domain.repositories.ProductPriceRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Optional;

@AllArgsConstructor
@Component
public class ProductPriceRepositoryDatabaseAdapter implements ProductPriceRepository {

    private final ProductPriceJPARepository productPriceJPARepository;
    private final ModelMapper modelMapper;

    @Override
    public Optional<ProductPrice> findProductPriceAppliedByDate(
            final Integer brandId,
            final Integer productId,
            final ZonedDateTime date
    ) {
        return productPriceJPARepository.findAppliedProductPriceByBrandAndProductAndDate(
                        brandId,
                        productId,
                        date)
                .map(entity -> modelMapper.map(entity, ProductPrice.class));
    }

}
