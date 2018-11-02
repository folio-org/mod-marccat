package org.folio.marccat.resources;

/**
 * System internal failure marker.
 *
 * @author cchiama
 * @since 1.0
 */
public class SystemInternalFailureException extends RuntimeException {
  /**
   * Builds a new {@link SystemInternalFailureException} with the given cause.
   *
   * @param exception the underlying cause.
   */
  public SystemInternalFailureException(final Throwable exception) {
    super(exception);
  }
}
