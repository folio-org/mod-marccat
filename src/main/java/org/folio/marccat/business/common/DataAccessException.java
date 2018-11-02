/*
 * (c) LibriCore
 *
 * Created on Jul 2, 2004
 *
 * DataAccessException.java
 */
package org.folio.marccat.business.common;

import lombok.Getter;
import lombok.Setter;
import org.folio.marccat.exception.ModCatalogingException;

import java.sql.SQLException;


/**
 * @author paulm
 * @version %I%, %G%
 * @since 1.0
 */
@Getter
@Setter
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
