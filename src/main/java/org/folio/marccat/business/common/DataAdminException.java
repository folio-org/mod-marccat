package org.folio.marccat.business.common;

public class DataAdminException extends Exception {
  private static final long serialVersionUID = -8207968816842862351L;

  public DataAdminException() {
    super();
  }

  public DataAdminException(String message, Throwable cause) {
    super(message, cause);
  }

  public DataAdminException(String message) {
    super(message);
  }

  public DataAdminException(Throwable cause) {
    super(cause);
  }
}
