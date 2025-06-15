package com.inditex.prices.infrastructure.adapters.inbound.rest.productprices;

import com.inditex.prices.infrastructure.adapters.inbound.rest.productprices.model.GetProductPriceRestRequest;
import com.inditex.prices.infrastructure.adapters.inbound.rest.productprices.model.GetProductPriceRestResponse;
import com.inditex.prices.usecase.GetProductPriceUseCase;
import com.inditex.prices.usecase.model.GetProductPriceRequest;
import com.inditex.prices.usecase.model.GetProductPriceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "prices API group")
@Validated
@RequiredArgsConstructor
@Slf4j
public class ProductPricesRestController {

    private final GetProductPriceUseCase getProductPriceUseCase;
    private final ModelMapper modelMapper;


    @GetMapping(value = "/product-prices", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(
            summary = "List/Search Prices",
            description = "REST API to list/search prices based on brand, date and product"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successful Operation",
            content = @Content(schema = @Schema(implementation = GetProductPriceRestResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    public GetProductPriceRestResponse getProductPrice(@ParameterObject @ModelAttribute GetProductPriceRestRequest request) {
        log.debug("processing GET product price request with parameters: {}", request);

        final GetProductPriceRequest useCaseReq = modelMapper.map(request, GetProductPriceRequest.class);
        final GetProductPriceResponse useCaseResp = getProductPriceUseCase.getProductPrice(useCaseReq);

        final GetProductPriceRestResponse response = modelMapper.map(useCaseResp, GetProductPriceRestResponse.class);
        log.debug("processed GET product price for request {} - with response {}", request, response);
        return response;
    }
}
