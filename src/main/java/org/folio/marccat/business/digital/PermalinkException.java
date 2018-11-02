package org.folio.cataloging.business.digital;

import org.folio.cataloging.exception.ModCatalogingException;

public class PermalinkException extends ModCatalogingException {

  private static final long serialVersionUID = 250170570458564169L;

  public PermalinkException() {
    super();
  }

  public PermalinkException(String message, Throwable cause) {
    super(message, cause);
  }

  public PermalinkException(String message) {
    super(message);
  }

  public PermalinkException(Throwable cause) {
    super(cause);
  }

}
