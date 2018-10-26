package org.folio.cataloging.business.descriptor;

import org.folio.cataloging.exception.ModCatalogingException;

public class UnsupportedHeadingException extends ModCatalogingException {

  public UnsupportedHeadingException() {
    super();
  }

  public UnsupportedHeadingException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnsupportedHeadingException(String message) {
    super(message);
  }

  public UnsupportedHeadingException(Throwable cause) {
    super(cause);
  }

}
