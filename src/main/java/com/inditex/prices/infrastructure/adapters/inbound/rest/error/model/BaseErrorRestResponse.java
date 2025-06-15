package com.inditex.prices.infrastructure.adapters.inbound.rest.error.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BaseErrorRestResponse {

    private ZonedDateTime timestamp;
    private String message;
    private Integer status;
    private String path;
}
