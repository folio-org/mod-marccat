package org.folio.marccat.integration.search;

import org.folio.marccat.exception.ModCatalogingException;

/**
 * Thrown in case a given expression cannot be parsed as a valid CCL.
 *
 * @author paulm
 * @author cchiama
 * @since 1.0
 */
public class CclParserException extends ModCatalogingException {
  private static final long serialVersionUID = 1L;

  /**
   * Builds a new {@link CclParserException} with the given message.
   *
   * @param message the exception message.
   */
  public CclParserException(final String message) {
    super(message);
  }
}
