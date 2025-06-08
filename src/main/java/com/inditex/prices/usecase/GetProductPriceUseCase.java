package com.inditex.prices.usecase;

import com.inditex.prices.domain.exception.ElementNotFoundException;
import com.inditex.prices.domain.model.ProductPrice;
import com.inditex.prices.domain.repositories.ProductPriceRepository;
import com.inditex.prices.usecase.model.GetProductPriceRequest;
import com.inditex.prices.usecase.model.GetProductPriceResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Slf4j
@Validated
@Transactional
@Component
@AllArgsConstructor
public class GetProductPriceUseCase {

    private final ProductPriceRepository productPriceRepository;
    private final ModelMapper modelMapper;

    public GetProductPriceResponse getProductPrice(@Valid @NotNull final GetProductPriceRequest request) {
        log.debug("getProductPriceRequest {}", request);
        final Optional<ProductPrice> appliedPrice = productPriceRepository.findProductPriceAppliedByDate(
                request.getBrandId(),
                request.getProductId(),
                request.getDate()
        );

        return appliedPrice
                .map(productPrice -> modelMapper.map(appliedPrice, GetProductPriceResponse.class))
                .orElseThrow(() -> new ElementNotFoundException(
                        ProductPrice.class,
                        request.getBrandId().toString(),
                        request.getProductId().toString(),
                        request.getDate().toString()
                ));

    }


}
