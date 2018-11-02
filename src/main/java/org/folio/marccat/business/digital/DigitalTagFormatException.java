package org.folio.marccat.business.digital;

import org.folio.marccat.exception.ModCatalogingException;

public class DigitalTagFormatException extends ModCatalogingException {

  public DigitalTagFormatException() {
    super();
  }

  public DigitalTagFormatException(String message, Throwable cause) {
    super(message, cause);
  }

  public DigitalTagFormatException(String message) {
    super(message);
  }

  public DigitalTagFormatException(Throwable cause) {
    super(cause);
  }

}
