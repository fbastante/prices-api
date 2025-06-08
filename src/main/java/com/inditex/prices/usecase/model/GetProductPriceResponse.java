package com.inditex.prices.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Currency;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProductPriceResponse {

    private Integer brandId;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private Integer priceList;
    private Integer productId;
    private Double price;
    private Currency currency;
}
