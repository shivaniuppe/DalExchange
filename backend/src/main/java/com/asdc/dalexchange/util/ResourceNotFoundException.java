package com.asdc.dalexchange.util;

/**
 * Custom exception class to indicate that a requested resource could not be found.
 * <p>
 * This exception extends {@link RuntimeException} and is typically used to signal
 * cases where a requested resource (such as an entity or data) is not available in the system.
 * </p>
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code ResourceNotFoundException} with the specified detail message.
     * <p>
     * The detail message is used to provide additional information about the exception.
     * </p>
     *
     * @param message the detail message, saved for later retrieval by the {@link #getMessage()} method.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
