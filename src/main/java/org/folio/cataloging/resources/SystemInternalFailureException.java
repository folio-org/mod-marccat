package org.folio.cataloging.resources;

/**
 * System internal failure marker.
 *
 * @author agazzarini
 * @since 1.0
 */
public class SystemInternalFailureException extends RuntimeException {
    /**
     * Builds a new {@link SystemInternalFailureException} with the given cause.
     *
     * @param exception the underlying cause.
     */
    public SystemInternalFailureException(final Throwable exception) {
        super(exception);
    }
}