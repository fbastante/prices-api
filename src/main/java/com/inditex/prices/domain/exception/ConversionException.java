package com.inditex.prices.domain.exception;

public class ConversionException extends BaseException {

    private static final String MESSAGE_TEMPLATE = "Error converting %s to %s: %s";

    public ConversionException(final Class<?> sourceClass, final Class<?> destinationClass, final String message) {
        super(buildMessage(sourceClass, destinationClass, message));
    }

    private static String buildMessage(
            final Class<?> sourceClass,
            final Class<?> destinationClass,
            final String message
    ) {
        return String.format(MESSAGE_TEMPLATE, sourceClass.getSimpleName(), destinationClass.getSimpleName(), message);
    }

}
