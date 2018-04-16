package org.folio.cataloging.resources;

public class SystemInternalFailureException extends RuntimeException {
    public SystemInternalFailureException(Throwable exception) {
        super(exception);
    }
}