package org.folio.cataloging.business.codetable;

/**
 * An {@link Avp} which contains additional info.
 *
 * @author paulm
 * @author agazzarini
 * @since 1.0
 */
public class AvpInfo<I> extends Avp <String> {
  private final I info;

  /**
   * Builds a new {@link AvpInfo} with the given data.
   *
   * @param value the
   * @param label
   */
  public AvpInfo(final String value, final String label, final I info) {
    super (value, label);
    this.info = info;
  }

  /**
   * Builds a new {@link AvpInfo} using the given prototype.
   *
   * @param avp the Avp prototype.
   */
  public AvpInfo(final Avp <String> avp, final I info) {
    this (avp.getValue ( ), avp.getLabel ( ), info);
  }

  /**
   * Returns the info associated with this {@link Avp}.
   *
   * @return the info associated with this {@link Avp}.
   */
  public I getInfo() {
    return info;
  }
}
