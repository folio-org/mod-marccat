package org.folio.marccat.exception;

/**
 * Base class for ModMarccat exceptions.
 *
 * @author cchiama
 */
public class ModMarccatException extends RuntimeException {

  public ModMarccatException() {
    super();
  }

  public ModMarccatException(final String message) {
    super(message);
  }

  public ModMarccatException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ModMarccatException(final Throwable cause) {
    super(cause);
  }

}
