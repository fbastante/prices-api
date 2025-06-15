package com.inditex.prices.infrastructure.adapters.inbound.rest.error;

import com.inditex.prices.domain.exception.ElementNotFoundException;
import com.inditex.prices.infrastructure.adapters.inbound.rest.error.model.BaseErrorRestResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class BaseRestControllerErrorHandler {


    @ExceptionHandler(value = { MethodValidationException.class, TypeMismatchException.class, ValidationException.class,
            HttpMessageConversionException.class})
    public ResponseEntity<Object> handleValidationException(final Exception ex, final HttpServletRequest request) {
        log.debug("handleValidationException with Exception {}", ex.getClass().getSimpleName());
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<Object> handleArgumentNotValidException(final MethodArgumentNotValidException ex,
                                                                  final HttpServletRequest request
    ) {
        log.debug("handleArgumentNotValidException with Exception {}", ex.getClass().getSimpleName());
        final String message = buildErrorMessageForFieldErrors(ex.getFieldErrors());

        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "validation error: " + message);
    }

    @ExceptionHandler(value = { HttpMessageNotReadableException.class })
    public ResponseEntity<Object> handleMessageNotReadable(final HttpMessageNotReadableException ex,
                                                           final HttpServletRequest request
    ) {
        log.debug("handleMessageNotReadable with Exception {}", ex.getClass().getSimpleName());
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, "Message is not readable: " + ex.getMessage());
    }


    @ExceptionHandler(value = { NoResourceFoundException.class, NoHandlerFoundException.class,
            ElementNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(final Exception ex, final HttpServletRequest request) {
        log.debug("handleNotFound with Exception {}", ex.getClass().getSimpleName());
        return buildErrorResponse(ex, request, HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(value = { MaxUploadSizeExceededException.class })
    public ResponseEntity<Object> handlePayloadTooLarge(final MaxUploadSizeExceededException ex,
                                                        final HttpServletRequest request
    ) {
        log.debug("handlePayloadTooLarge with Exception {}", ex.getClass().getSimpleName());
        return buildErrorResponse(ex, request, HttpStatus.PAYLOAD_TOO_LARGE, ex.getMessage());
    }

    @ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
    public ResponseEntity<Object> handleMethodNotAllowed(final HttpRequestMethodNotSupportedException ex,
                                                         final HttpServletRequest request
    ) {
        log.debug("handleMethodNotAllowed with Exception {}", ex.getClass().getSimpleName());
        return buildErrorResponse(ex, request, HttpStatus.METHOD_NOT_ALLOWED,
                String.format("method '%s' not supported", ex.getMethod())
        );
    }

    @ExceptionHandler(value = { HttpMediaTypeNotAcceptableException.class })
    public ResponseEntity<Object> handleNotAcceptable(final HttpMediaTypeNotAcceptableException ex,
                                                      final HttpServletRequest request
    ) {
        log.debug("handleNotAcceptable with Exception {}", ex.getClass().getSimpleName());
        return buildErrorResponse(ex, request, HttpStatus.NOT_ACCEPTABLE,
                String.format("media type '%s' not acceptable, use %s",
                        request.getHeader("Accept"), ex.getSupportedMediaTypes()));
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleInternalServerError(final Exception ex, final HttpServletRequest request) {
        log.error("handling Internal Error {}", ex.getClass().getSimpleName(), ex);
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    private String buildErrorMessageForFieldErrors(final List<FieldError> fieldErrorList) {
        return fieldErrorList.stream().map(fieldError -> String.format("field '%s' with invalid value '%s'",
                fieldError.getField(), fieldError.getRejectedValue()))
                .collect(Collectors.joining("; "));
    }

    private ResponseEntity<Object> buildErrorResponse(
            final Exception ex, final HttpServletRequest request, final HttpStatus status, final String message) {
        log.debug("creating error response with message: {}", ex.getMessage());
        final BaseErrorRestResponse responseObj = BaseErrorRestResponse.builder()
                .timestamp(ZonedDateTime.now())
                .status(status.value())
                .message(message)
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(responseObj, new HttpHeaders(), HttpStatusCode.valueOf(status.value()));
    }
}
