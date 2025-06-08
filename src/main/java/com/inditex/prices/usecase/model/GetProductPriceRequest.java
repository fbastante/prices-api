package com.inditex.prices.usecase.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProductPriceRequest {

    @NotNull
    private Integer brandId;

    @NotNull
    private Integer productId;

    @NotNull
    private ZonedDateTime date;
}
