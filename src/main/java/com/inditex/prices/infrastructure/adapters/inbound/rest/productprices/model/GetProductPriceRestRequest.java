package com.inditex.prices.infrastructure.adapters.inbound.rest.productprices.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Schema(description = "Object containing needed parameters to search for product prices in a date")
@Data
@Builder
@AllArgsConstructor
public class GetProductPriceRestRequest {

    @Schema(
            description = "ID of the product's brand",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "1"
    )
    @NotNull
    private Integer brandId;

    @Schema(
            description = "ID of the product",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "1"
    )
    @NotNull
    private Integer productId;

    @Schema(
            description = "Date during the price would be applied",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "2024-06-05T18:12:18Z"
    )
    @NotNull
    private ZonedDateTime date;
}
