package org.folio.marccat.exception;

public class TenantAlreadyExistsException extends RuntimeException {

  private static final long serialVersionUID = -3572834219864312625L;

  public TenantAlreadyExistsException(String message) {
    super(message);
  }

}
