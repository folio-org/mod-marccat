package org.folio.marccat.dao.persistence;

import java.io.Serializable;


public class RecordTypeMaterial implements Serializable {
  private String recordTypeCode;
  private String bibliographicLevel;
  private String amicusMaterialTypeCode;
  private int bibHeader008;
  private int bibHeader006;


  public String getAmicusMaterialTypeCode() {
    return amicusMaterialTypeCode;
  }


  public void setAmicusMaterialTypeCode(String string) {
    amicusMaterialTypeCode = string;
  }


  public int getBibHeader006() {
    return bibHeader006;
  }


  public void setBibHeader006(int s) {
    bibHeader006 = s;
  }


  public int getBibHeader008() {
    return bibHeader008;
  }


  public void setBibHeader008(int s) {
    bibHeader008 = s;
  }


  public String getBibliographicLevel() {
    return bibliographicLevel;
  }


  public void setBibliographicLevel(String string) {
    bibliographicLevel = string;
  }


  public String getRecordTypeCode() {
    return recordTypeCode;
  }


  public void setRecordTypeCode(String string) {
    recordTypeCode = string;
  }


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
