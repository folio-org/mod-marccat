package org.folio.cataloging.exception;

/**
 * Base class for ModCataloging exceptions.
 *
 * @author agazzarini
 * @author paulm
 * @since 1.0
 */
public class ModCatalogingException extends RuntimeException {

	public ModCatalogingException() {
		super();
	}

	public ModCatalogingException(final String message) {
		super(message);
	}

	public ModCatalogingException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ModCatalogingException(final Throwable cause) {
		super(cause);
	}

}
