package org.folio.marccat.shared;

import java.io.Serializable;

/**
 * @author elena
 * @author cchiama
 * @since 1.0
 */
public class ValidationKey implements Serializable {
  private String marcTag;
  private int marcTagCategoryCode;

  /**
   * Builds a new {@link ValidationKey}.
   */
  public ValidationKey() {
  }

  /**
   * Builds a new {@link ValidationKey} with the given data.
   *
   * @param marcTag         the tag code.
   * @param marcTagCategory the tag category.
   */
  public ValidationKey(final String marcTag, final int marcTagCategory) {
    this.setMarcTag(marcTag);
    this.setMarcTagCategoryCode(marcTagCategory);
  }

  @Override
  public boolean equals(final Object o) {
    return (o instanceof ValidationKey)
      && marcTag.equals(((ValidationKey) o).getMarcTag())
      && marcTagCategoryCode == ((ValidationKey) o).getMarcTagCategoryCode();
  }

  @Override
  public int hashCode() {
    return marcTag.hashCode() + (11 * marcTagCategoryCode);
  }

  /**
   * Returns the tag code.
   *
   * @return the tag code.
   */
  public String getMarcTag() {
    return marcTag;
  }

  /**
   * Sets the tag code.
   *
   * @param code the tag code.
   */
  private void setMarcTag(String code) {
    marcTag = code;
  }

  /**
   * Returns the tag category.
   *
   * @return the tag category.
   */
  public int getMarcTagCategoryCode() {
    return marcTagCategoryCode;
  }

  /**
   * Sets the tag category.
   *
   * @param category the tag category.
   */
  private void setMarcTagCategoryCode(int category) {
    marcTagCategoryCode = category;
  }

  @Override
  public String toString() {
    return "[ tag: '" + marcTag + "' category: '" + marcTagCategoryCode + "' ]";
  }
}
