package com.github.microtweak.jac4e.core.exception;

public class EnumMetadataException extends RuntimeException {

    public EnumMetadataException(String message) {
        super(message);
    }

    public EnumMetadataException(String message, Object... args) {
        this(String.format(message, args));
    }

}
