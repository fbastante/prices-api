package com.inditex.prices.domain.repositories;

import com.inditex.prices.domain.model.ProductPrice;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.time.ZonedDateTime;
import java.util.Optional;

@Validated
public interface ProductPriceRepository {

    Optional<ProductPrice> findProductPriceAppliedByDate(
            @NotNull Integer brandId,
            @NotNull Integer productId,
            @NotNull ZonedDateTime date
    );

}
