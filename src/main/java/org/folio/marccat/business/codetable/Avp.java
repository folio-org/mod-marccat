package org.folio.marccat.business.codetable;

import java.io.Serializable;

/**
 * Holds a single attribute-stringValue pair (AVP).
 *
 * @author paulm
 * @author cchiama
 * @since 1.0
 */
public class Avp<V> implements Comparable<Avp>, Serializable {
  private V value;
  private String label;

  public Avp() {
  }

  /**
   * Builds a new {@link Avp} with the given pair.
   *
   * @param value the element stringValue.
   * @param label the element label.
   */
  public Avp(final V value, final String label) {
    this.value = value;
    this.label = label;
  }


  /**
   * Returns the label associated with this pair.
   *
   * @return the label associated with this pair.
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets the label of this pair.
   *
   * @param label the pair label.
   */
  public void setLabel(final String label) {
    this.label = label;
  }

  /**
   * Returns the stringValue associated with this pair.
   *
   * @return the stringValue associated with this pair.
   */
  public V getValue() {
    return value;
  }

  /**
   * Sets the stringValue of this pair.
   *
   * @param value the pair label.
   */
  public void setValue(final V value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Avp)
      && ((Avp) obj).value.equals(value)
      && ((Avp) obj).label.equals(label);
  }

  @Override
  public int hashCode() {
    return value.hashCode() + label.hashCode();
  }

  @Override
  public String toString() {
    return "(" + value + " = " + label + ")";
  }

  @Override
  public int compareTo(final Avp pair) {
    if (pair == null) return -1;
    return label.compareTo(pair.getLabel());
  }
}
