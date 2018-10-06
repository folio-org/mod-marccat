package org.folio.cataloging.business.common;

public class ErrorIsbnProcException extends Exception {
  private static final long serialVersionUID = -8207968816842862351L;

  public ErrorIsbnProcException() {
    super ( );
  }

  public ErrorIsbnProcException(String message, Throwable cause) {
    super (message, cause);
  }

  public ErrorIsbnProcException(String message) {
    super (message);
  }

  public ErrorIsbnProcException(Throwable cause) {
    super (cause);
  }
}
