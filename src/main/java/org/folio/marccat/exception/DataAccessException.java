/*
 * (c) LibriCore
 *
 * Created on Jul 2, 2004
 *
 * DataAccessException.java
 */
package org.folio.marccat.business.common;

import org.folio.marccat.exception.ModCatalogingException;

import java.sql.SQLException;


/**
 * @author paulm
 * @version %I%, %G%
 * @since 1.0
 */
public class DataAccessException extends ModCatalogingException {


  public int errorCode;
  public String state;

  /**
   * @see ModCatalogingException#ModCatalogingException()
   */
  public DataAccessException() {
    super();
  }

  /**
   * @see ModCatalogingException#ModCatalogingException(String)
   */
  public DataAccessException(String message) {
    super(message);
  }

  /**
   * @see ModCatalogingException#ModCatalogingException(String, Throwable)
   */
  public DataAccessException(String message, Throwable cause) {
    super(message, cause);
    getCodeAndState(cause);
  }

  /**
   * @see ModCatalogingException#ModCatalogingException(Throwable)
   */
  public DataAccessException(Throwable cause) {
    super(cause);
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  private void getCodeAndState(Throwable cause) {
    if (cause instanceof SQLException) {
      errorCode = ((SQLException) cause).getErrorCode();
      state = ((SQLException) cause).getSQLState();
    } else {
      errorCode = -1;
      state = cause.getMessage();
    }
  }


  @Override
  public String toString() {
    return new StringBuilder(
      getClass().getName())
      .append('[')
      .append(errorCode)
      .append(',')
      .append(state).append("]: ")
      .append(getLocalizedMessage())
      .toString();
  }

}
