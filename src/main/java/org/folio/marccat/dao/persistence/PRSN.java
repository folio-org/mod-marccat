/*
 * (c) LibriCore
 *
 * Created on 02-nov-2004
 *
 * PRSN.java
 */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Maite
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class PRSN implements Serializable {
  private int personalNumber;
  private short lockingVersionUpdateNumber;
  private Integer functionCustomerNumber;
  private Short billingGroupSequenceNumber;
  private Float functionDepositAcountAmount;
  private Short personalTitleCode;
  private short languagePrefunctionCode;
  private Short billingGroupFirstCode;
  private char functionAcountStandingIndicator;
  private char pstExpeditionIndicator;
  private char gstExpeditionIndicator;
  private char provincialServiceTaxesExpeditionIndicator;
  private String functionAcountStandingNote;
  private String personalSurnameShortForm;
  private String personal1stNameShortForm;
  private String personal1stName;
  private String personalInitialName;
  private String personalSurnameName;
  private String personalMiddleName;
  private Date birthDate;
  private Date creationDate;
  private short repMailIndex;

  public PRSN() {
    super();
  }

  public PRSN(int personalNbr, short lockingVersion, short lang, char fAccount,
              char pst, char gst, char provincial, String surnameShort, String firstNShort, String initials,
              String middle, Date circulation, short rep) {
    this.personalNumber = personalNbr;
    this.lockingVersionUpdateNumber = lockingVersion;
    this.languagePrefunctionCode = lang;
    this.functionAcountStandingIndicator = fAccount;
    this.pstExpeditionIndicator = pst;
    this.gstExpeditionIndicator = gst;
    this.provincialServiceTaxesExpeditionIndicator = provincial;
    this.personalSurnameShortForm = surnameShort;
    this.personal1stNameShortForm = firstNShort;
    this.personalInitialName = initials;
    this.personalMiddleName = middle;
    this.creationDate = circulation;
    this.repMailIndex = rep;
  }


  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Short getBillingGroupFirstCode() {
    return billingGroupFirstCode;
  }

  /**
   * @param short1
   * @throws
   * @since 1.0
   */
  public void setBillingGroupFirstCode(Short short1) {
    billingGroupFirstCode = short1;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Short getBillingGroupSequenceNumber() {
    return billingGroupSequenceNumber;
  }

  /**
   * @param short1
   * @throws
   * @since 1.0
   */
  public void setBillingGroupSequenceNumber(Short short1) {
    billingGroupSequenceNumber = short1;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Date getBirthDate() {
    return birthDate;
  }

  /**
   * @param date
   * @throws
   * @since 1.0
   */
  public void setBirthDate(Date date) {
    birthDate = date;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public String getFunctionAcountStandingNote() {
    return functionAcountStandingNote;
  }

  /**
   * @param string
   * @throws
   * @since 1.0
   */
  public void setFunctionAcountStandingNote(String string) {
    functionAcountStandingNote = string;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Integer getFunctionCustomerNumber() {
    return functionCustomerNumber;
  }

  /**
   * @param integer
   * @throws
   * @since 1.0
   */
  public void setFunctionCustomerNumber(Integer integer) {
    functionCustomerNumber = integer;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Float getFunctionDepositAcountAmount() {
    return functionDepositAcountAmount;
  }

  /**
   * @param float1
   * @throws
   * @since 1.0
   */
  public void setFunctionDepositAcountAmount(Float float1) {
    functionDepositAcountAmount = float1;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public char getGstExpeditionIndicator() {
    return gstExpeditionIndicator;
  }

  /**
   * @param c
   * @throws
   * @since 1.0
   */
  public void setGstExpeditionIndicator(char c) {
    gstExpeditionIndicator = c;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public short getLanguagePrefunctionCode() {
    return languagePrefunctionCode;
  }

  /**
   * @param s
   * @throws
   * @since 1.0
   */
  public void setLanguagePrefunctionCode(short s) {
    languagePrefunctionCode = s;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public short getLockingVersionUpdateNumber() {
    return lockingVersionUpdateNumber;
  }

  /**
   * @param s
   * @throws
   * @since 1.0
   */
  public void setLockingVersionUpdateNumber(short s) {
    lockingVersionUpdateNumber = s;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public String getPersonal1stName() {
    return personal1stName;
  }

  /**
   * @param string
   * @throws
   * @since 1.0
   */
  public void setPersonal1stName(String string) {
    personal1stName = string;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public String getPersonal1stNameShortForm() {
    return personal1stNameShortForm;
  }

  /**
   * @param string
   * @throws
   * @since 1.0
   */
  public void setPersonal1stNameShortForm(String string) {
    personal1stNameShortForm = string;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public String getPersonalInitialName() {
    return personalInitialName;
  }

  /**
   * @param string
   * @throws
   * @since 1.0
   */
  public void setPersonalInitialName(String string) {
    personalInitialName = string;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public String getPersonalMiddleName() {
    return personalMiddleName;
  }

  /**
   * @param string
   * @throws
   * @since 1.0
   */
  public void setPersonalMiddleName(String string) {
    personalMiddleName = string;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public int getPersonalNumber() {
    return personalNumber;
  }

  /**
   * @param i
   * @throws
   * @since 1.0
   */
  public void setPersonalNumber(int i) {
    personalNumber = i;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public String getPersonalSurnameName() {
    return personalSurnameName;
  }

  /**
   * @param string
   * @throws
   * @since 1.0
   */
  public void setPersonalSurnameName(String string) {
    personalSurnameName = string;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public String getPersonalSurnameShortForm() {
    return personalSurnameShortForm;
  }

  /**
   * @param string
   * @throws
   * @since 1.0
   */
  public void setPersonalSurnameShortForm(String string) {
    personalSurnameShortForm = string;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Short getPersonalTitleCode() {
    return personalTitleCode;
  }

  /**
   * @param short1
   * @throws
   * @since 1.0
   */
  public void setPersonalTitleCode(Short short1) {
    personalTitleCode = short1;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public char getPstExpeditionIndicator() {
    return pstExpeditionIndicator;
  }

  /**
   * @param c
   * @throws
   * @since 1.0
   */
  public void setPstExpeditionIndicator(char c) {
    pstExpeditionIndicator = c;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public short getRepMailIndex() {
    return repMailIndex;
  }

  /**
   * @param s
   * @throws
   * @since 1.0
   */
  public void setRepMailIndex(short s) {
    repMailIndex = s;
  }

  /**
   * @return Returns the creationDate.
   */
  public Date getCreationDate() {
    return creationDate;
  }

  /**
   * @param creationDate The creationDate to set.
   */
  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public char getFunctionAcountStandingIndicator() {
    return functionAcountStandingIndicator;
  }

  /**
   * @param c
   * @throws
   * @since 1.0
   */
  public void setFunctionAcountStandingIndicator(char c) {
    functionAcountStandingIndicator = c;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public char getProvincialServiceTaxesExpeditionIndicator() {
    return provincialServiceTaxesExpeditionIndicator;
  }

  /**
   * @param c
   * @throws
   * @since 1.0
   */
  public void setProvincialServiceTaxesExpeditionIndicator(char c) {
    provincialServiceTaxesExpeditionIndicator = c;
  }

}
