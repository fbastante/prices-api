package com.inditex.prices.domain.exception;

public class ElementNotFoundException extends BaseException {

    private static final String MESSAGE_TEMPLATE = "Element %s wasn't found for values %s";

    public ElementNotFoundException(final Class<?> element, final String... values) {
        super(buildMessage(element, values));
    }

    private static String buildMessage(final Class<?> element, final String... values) {
        return String.format(MESSAGE_TEMPLATE, element.getSimpleName(), String.join(", ", values));
    }

}
