package org.folio.cataloging.exception;

public class CreateHeadingException extends ModCatalogingException {
  private static final long serialVersionUID = 3742203512724962635L;

  public CreateHeadingException() {
    super();
  }

  public CreateHeadingException(String message) {
    super(message);
  }

  public CreateHeadingException(String message, Throwable cause) {
    super(message, cause);
  }

  public CreateHeadingException(Throwable cause) {
    super(cause);
  }
}
