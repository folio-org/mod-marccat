/*
 * (c) LibriCore
 *
 * Created on Jul 2, 2004
 *
 * DataAccessException.java
 */
package org.folio.marccat.exception;

import java.sql.SQLException;


/**
 * @author paulm
 * @version %I%, %Global%
 * @since 1.0
 */
public class DataAccessException extends ModMarccatException {


  private int errorCode;
  public String state;

  /**
   * @see ModMarccatException#ModMarccatException()
   */
  public DataAccessException() {
    super();
  }

  /**
   * @see ModMarccatException#ModMarccatException(String)
   */
  public DataAccessException(String message) {
    super(message);
  }

  /**
   * @see ModMarccatException#ModMarccatException(String, Throwable)
   */
  public DataAccessException(String message, Throwable cause) {
    super(message, cause);
    getCodeAndState(cause);
  }

  /**
   * @see ModMarccatException#ModMarccatException(Throwable)
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
    return getClass().getName() +
      '[' +
      errorCode +
      ',' +
      state + "]: " +
      getLocalizedMessage();
  }

}
