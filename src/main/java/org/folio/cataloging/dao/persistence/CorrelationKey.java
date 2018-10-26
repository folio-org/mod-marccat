package org.folio.cataloging.dao.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

/**
 * @author elena
 * @since 1.0
 */
public class CorrelationKey implements Serializable {

  private static final Log logger = LogFactory.getLog(CorrelationKey.class);

  private String marcTag;
  private char marcFirstIndicator;
  private char marcSecondIndicator;
  private int marcTagCategoryCode;

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public CorrelationKey() {
    super();
  }

  public CorrelationKey(final String marcTag,
                        final char ind1,
                        final char ind2,
                        final int marcCategory) {

    this.setMarcTag(marcTag);
    this.setMarcFirstIndicator(ind1);
    this.setMarcSecondIndicator(ind2);
    this.setMarcTagCategoryCode(marcCategory);
  }

  /**
   * override equals and hashcode for hibernate key comparison
   */

  public boolean equals(Object anObject) {
    if (anObject instanceof CorrelationKey) {
      CorrelationKey aKey =
        (CorrelationKey) anObject;
      return (
        marcTag.equals(aKey.getMarcTag())
          && marcFirstIndicator == aKey.getMarcFirstIndicator()
          && marcSecondIndicator == aKey.getMarcSecondIndicator()
          && marcTagCategoryCode == aKey.getMarcTagCategoryCode());
    } else {
      return false;
    }
  }

  public int hashCode() {
    return marcTag.hashCode()
      + (3 * marcFirstIndicator)
      + (5 * marcSecondIndicator)
      + (7 * marcTagCategoryCode);
  }

  public int getMarcTagCategoryCode() {
    return marcTagCategoryCode;
  }

  private void setMarcTagCategoryCode(int s) {
    marcTagCategoryCode = s;
  }

  public char getMarcFirstIndicator() {
    return marcFirstIndicator;
  }

  private void setMarcFirstIndicator(char c) {
    marcFirstIndicator = c;
  }

  public String getMarcTag() {
    return marcTag;
  }

  private void setMarcTag(String s) {
    marcTag = s;
  }

  public char getMarcSecondIndicator() {
    return marcSecondIndicator;
  }

  private void setMarcSecondIndicator(char c) {
    marcSecondIndicator = c;
  }

  public CorrelationKey changeSkipInFilingIndicator(int skip) {
    if (marcFirstIndicator == 'S') return changeFirstIndicator(Integer.toString(skip).charAt(0));
    if (marcSecondIndicator == 'S') return changeSecondIndicator(Integer.toString(skip).charAt(0));
    return this;
  }

  public CorrelationKey changeAuthoritySourceIndicator(int source) {
    logger.debug("changeAuthoritySource: " + getMarcSecondIndicator());
    if (marcFirstIndicator == 'O') {
      return changeFirstIndicator(T_AUT_HDG_SRC.toMarcIndicator(source));
    }
    if (marcSecondIndicator == 'O') {
      logger.debug("changing to " + T_AUT_HDG_SRC.toMarcIndicator(source));
      return changeSecondIndicator(T_AUT_HDG_SRC.toMarcIndicator(source));
    }
    return this;
  }

  public CorrelationKey changeFirstIndicator(char c) {
    return new CorrelationKey(marcTag, c, marcSecondIndicator, marcTagCategoryCode);
  }

  public CorrelationKey changeSecondIndicator(char c) {
    return new CorrelationKey(marcTag, marcFirstIndicator, c, marcTagCategoryCode);
  }

  @Override
  public String toString() {
    return "CorrelationKey('" + getMarcTag() + getMarcFirstIndicator() + getMarcSecondIndicator() + "')";
  }

}
