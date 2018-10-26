package org.folio.cataloging.business.common;

public class DateInputException extends Exception {
  private static final long serialVersionUID = -8207968816842862351L;

  public DateInputException() {
    super();
  }

  public DateInputException(String message, Throwable cause) {
    super(message, cause);
  }

  public DateInputException(String message) {
    super(message);
  }

  public DateInputException(Throwable cause) {
    super(cause);
  }
}
