package org.folio.marccat.business.digital;

import org.folio.marccat.exception.ModCatalogingException;

public class DigitalLevelException extends ModCatalogingException {

  private static final long serialVersionUID = -5156470499299759964L;

  public DigitalLevelException() {
    super();
  }

  public DigitalLevelException(String message, Throwable cause) {
    super(message, cause);
  }

  public DigitalLevelException(String message) {
    super(message);
  }

  public DigitalLevelException(Throwable cause) {
    super(cause);
  }

}
