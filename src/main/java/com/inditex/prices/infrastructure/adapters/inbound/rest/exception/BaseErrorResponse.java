package com.inditex.prices.infrastructure.adapters.inbound.rest.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BaseErrorResponse {

    private ZonedDateTime timestamp;
    private Integer status;
    private String error;
    private String path;
}
