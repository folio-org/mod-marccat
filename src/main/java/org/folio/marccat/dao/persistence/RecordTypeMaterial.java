package org.folio.marccat.dao.persistence;

import java.io.Serializable;


public class RecordTypeMaterial implements Serializable {
  private String recordTypeCode;
  private String bibliographicLevel;
  private String amicusMaterialTypeCode;
  private int bibHeader008;
  private int bibHeader006;

  /**
   * @since 1.0
   */
  public String getAmicusMaterialTypeCode() {
    return amicusMaterialTypeCode;
  }

  /**
   * @since 1.0
   */
  public void setAmicusMaterialTypeCode(String string) {
    amicusMaterialTypeCode = string;
  }

  /**
   * @since 1.0
   */
  public int getBibHeader006() {
    return bibHeader006;
  }

  /**
   * @since 1.0
   */
  public void setBibHeader006(int s) {
    bibHeader006 = s;
  }

  /**
   * @since 1.0
   */
  public int getBibHeader008() {
    return bibHeader008;
  }

  /**
   * @since 1.0
   */
  public void setBibHeader008(int s) {
    bibHeader008 = s;
  }

  /**
   * @since 1.0
   */
  public String getBibliographicLevel() {
    return bibliographicLevel;
  }

  /**
   * @since 1.0
   */
  public void setBibliographicLevel(String string) {
    bibliographicLevel = string;
  }

  /**
   * @since 1.0
   */
  public String getRecordTypeCode() {
    return recordTypeCode;
  }

  /**
   * @since 1.0
   */
  public void setRecordTypeCode(String string) {
    recordTypeCode = string;
  }

  /**
   * @since 1.0
   */
  public Character getRecordTypeCodeCharacter() {
    if (recordTypeCode == null) {
      return null;
    } else {
      return recordTypeCode.charAt(0);
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object arg0) {
    if (arg0 instanceof RecordTypeMaterial) {
      RecordTypeMaterial rtm = (RecordTypeMaterial) arg0;
      if (getBibliographicLevel() == null) {
        if (rtm.getBibliographicLevel() == null) {
          return getRecordTypeCode() == rtm.getRecordTypeCode();
        } else {
          return false;
        }
      } else {
        if (rtm.getBibliographicLevel() == null) {
          return false;
        } else {
          return getRecordTypeCode() == rtm.getRecordTypeCode();
        }
      }
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    if (getBibliographicLevel() == null) {
      return getRecordTypeCode().hashCode();
    } else {
      return getRecordTypeCode().hashCode()
        + getBibliographicLevel().hashCode();
    }
  }

}
