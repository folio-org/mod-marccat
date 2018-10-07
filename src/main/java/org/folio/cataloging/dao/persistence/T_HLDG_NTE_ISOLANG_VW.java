/*
 * (c) LibriCore
 *
 * Created on 29-jul-2004
 *
 * T_HLDG_NTE_ISOLANG_VW.java
 */
package org.folio.cataloging.dao.persistence;

import java.util.Date;

/**
 * @author elena
 * @version $Revision: 1.3 $, $Date: 2004/08/12 15:05:56 $
 * @since 1.0
 */
public class T_HLDG_NTE_ISOLANG_VW {

  private short code;
  private short sequence;
  private short repeteable;
  private String isoLanguage;
  private String labelStringText;
  private Date lastUpdateDate;
  private short tableObsoletoIndex;
  private Date tableObsoletoDate;

  public short getCode() {
    return code;
  }

  public void setCode(short s) {
    code = s;
  }

  public String getIsoLanguage() {
    return isoLanguage;
  }

  public void setIsoLanguage(String string) {
    isoLanguage = string;
  }

  public String getLabelStringText() {
    return labelStringText;
  }

  public void setLabelStringText(String string) {
    labelStringText = string;
  }

  public Date getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(Date date) {
    lastUpdateDate = date;
  }

  public short getRepeteable() {
    return repeteable;
  }

  public void setRepeteable(short s) {
    repeteable = s;
  }

  public short getSequence() {
    return sequence;
  }

  public void setSequence(short s) {
    sequence = s;
  }

  public Date getTableObsoletoDate() {
    return tableObsoletoDate;
  }

  public void setTableObsoletoDate(Date date) {
    tableObsoletoDate = date;
  }

  public short getTableObsoletoIndex() {
    return tableObsoletoIndex;
  }

  public void setTableObsoletoIndex(short s) {
    tableObsoletoIndex = s;
  }

}
