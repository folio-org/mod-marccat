package org.folio.marccat.business.digital;

import org.folio.marccat.exception.ModCatalogingException;

public class DigitalFileSystemException extends ModCatalogingException {

  public DigitalFileSystemException() {
    super();
  }

  public DigitalFileSystemException(String message, Throwable cause) {
    super(message, cause);
  }

  public DigitalFileSystemException(String message) {
    super(message);
  }

  public DigitalFileSystemException(Throwable cause) {
    super(cause);
  }

}
