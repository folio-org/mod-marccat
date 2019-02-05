package org.folio.marccat.config.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * OKAPI Cataloging module Logging utility.
 */
public class Log {
  private final Logger logger;

  /**
   * Builds a new Log for the given class owner.
   *
   * @param clazz the class owner.
   */
  public Log(final Class clazz) {
    logger = LogManager.getLogger(clazz);
  }

  /**
   * Logs out an error message.
   *
   * @param owner   the class owner.
   * @param message the message (with no placeholders).
   */
  public static void error(final Class owner, final String message) {
    new Log(owner).error(message);
  }

  /**
   * Logs out an INFO message.
   *
   * @param message the message (with optional placeholders).
   * @param values  the placeholders values.
   */
  public void info(final String message, final Object... values) {
    logger.info(values == null ? message : String.format(message, values));
  }

  /**
   * Logs out a DEBUG message.
   *
   * @param message the message (with optional placeholders).
   * @param values  the placeholders values.
   */
  public void debug(final String message, final Object... values) {
    if (logger.isDebugEnabled()) {
      logger.debug(values == null ? message : String.format(message, values));
    }
  }

  /**
   * Logs out an ERROR message.
   *
   * @param message the message (with optional placeholders).
   * @param values  the placeholders values.
   */
  public void error(final String message, final Object... values) {
    logger.error(values == null ? message : String.format(message, values));
  }

  /**
   * Logs out an error message.
   *
   * @param message the message (with no placeholders).
   * @param cause   the exception cause.
   */
  public void error(final String message, final Throwable cause) {
    logger.error(message, cause);
  }

  /**
   * Logs out an error message.
   *
   * @param message the message (with no placeholders).
   * @param cause   the exception cause.
   */
  public void error(final String message, final Throwable cause, final Object... values) {
    logger.error(String.format(message, values), cause);
  }
}
