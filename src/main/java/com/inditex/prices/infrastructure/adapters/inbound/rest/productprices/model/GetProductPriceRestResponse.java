package com.inditex.prices.infrastructure.adapters.inbound.rest.productprices.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Schema(description = "Object containing the returned information of the product price")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetProductPriceRestResponse {

    @Schema(
            description = "ID of the product's brand",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "1"
    )
    private Integer brandId;

    @Schema(
            description = "ID of the product",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "1"
    )
    private Integer productId;

    @Schema(
            description = "Start date of the period when the price will be applied",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "2024-06-05T18:12:18Z"
    )
    private ZonedDateTime startDate;

    @Schema(
            description = "End date of the period when the price will be applied",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "2024-06-05T18:12:18Z"
    )
    private ZonedDateTime endDate;

    @Schema(
            description = "ID of the price rate that is applied",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "1"
    )
    private Integer priceList;

    @Schema(
            description = "Finally applied price",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "35.50"
    )
    private Double price;

    @Schema(
            description = "ISO code of the price currency",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "EUR"
    )
    private String currency;
}
