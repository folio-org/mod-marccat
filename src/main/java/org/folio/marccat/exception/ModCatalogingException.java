package org.folio.marccat.exception;

/**
 * Base class for ModMarccat exceptions.
 *
 * @author cchiama
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
