package com.inditex.prices.infrastructure.adapters.inbound.rest.exception;

import com.inditex.prices.domain.exception.ElementNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@Slf4j
@NoArgsConstructor
public abstract class BaseRestController {

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<BaseErrorResponse> handleElementNotFound(final HttpServletRequest req, final ElementNotFoundException ex) {
        log.debug("handling exception {}", ex.getClass().getSimpleName());

        final BaseErrorResponse body = BaseErrorResponse.builder()
                .timestamp(ZonedDateTime.now())
                .error(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(req.getRequestURI())
                .build();

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
