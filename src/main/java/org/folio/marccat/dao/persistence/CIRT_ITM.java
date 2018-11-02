/*
 * (c) LibriCore
 *
 * Created on 20-jul-2004
 *
 * CIRT_ITM.java
 */
package org.folio.marccat.dao.persistence;

import java.util.Date;

/**
 * @author elena
 * @version $Revision: 1.4 $, $Date: 2004/08/04 12:44:20 $
 * @since 1.0
 */
public class CIRT_ITM {
  private int copyIdNumber;
  private int organisationNumber;
  private int bibItemNumber;
  private int personNumber;
  private String barCodeNumber;
  private short circulationStatesticsTypeCode;
  private Date circulationItemChargeOutDate;
  private Date circulationItemDueDate;
  private Date circulationItemPreviousDueDate;
  private Date circulationItemRecallNotificationDate;
  private Date circulationItemFirstOverDate;
  private Date circulationItemSecondOverDate;
  private Date circulationItemThirdOverDate;
  private int circulationItemRenewalCenterCount;
  private char circulationItemExtendLoanIndex;
  private char circulationItemIllIndex;
  private Character circulationItemTranstationIndex;
  private Integer circulationItemChekingBranch;

  public String getBarCodeNumber() {
    return barCodeNumber;
  }

  public void setBarCodeNumber(String string) {
    barCodeNumber = string;
  }

  public int getBibItemNumber() {
    return bibItemNumber;
  }

  public void setBibItemNumber(int i) {
    bibItemNumber = i;
  }

  public Date getCirculationItemChargeOutDate() {
    return circulationItemChargeOutDate;
  }

  public void setCirculationItemChargeOutDate(Date date) {
    circulationItemChargeOutDate = date;
  }

  public Integer getCirculationItemChekingBranch() {
    return circulationItemChekingBranch;
  }

  public void setCirculationItemChekingBranch(Integer integer) {
    circulationItemChekingBranch = integer;
  }

  public Date getCirculationItemDueDate() {
    return circulationItemDueDate;
  }

  public void setCirculationItemDueDate(Date date) {
    circulationItemDueDate = date;
  }

  public char getCirculationItemExtendLoanIndex() {
    return circulationItemExtendLoanIndex;
  }

  public void setCirculationItemExtendLoanIndex(char c) {
    circulationItemExtendLoanIndex = c;
  }

  public Date getCirculationItemFirstOverDate() {
    return circulationItemFirstOverDate;
  }

  public void setCirculationItemFirstOverDate(Date date) {
    circulationItemFirstOverDate = date;
  }

  public char getCirculationItemIllIndex() {
    return circulationItemIllIndex;
  }

  public void setCirculationItemIllIndex(char c) {
    circulationItemIllIndex = c;
  }

  public Date getCirculationItemPreviousDueDate() {
    return circulationItemPreviousDueDate;
  }

  public void setCirculationItemPreviousDueDate(Date date) {
    circulationItemPreviousDueDate = date;
  }

  public Date getCirculationItemRecallNotificationDate() {
    return circulationItemRecallNotificationDate;
  }

  public void setCirculationItemRecallNotificationDate(Date date) {
    circulationItemRecallNotificationDate = date;
  }

  public int getCirculationItemRenewalCenterCount() {
    return circulationItemRenewalCenterCount;
  }

  public void setCirculationItemRenewalCenterCount(int i) {
    circulationItemRenewalCenterCount = i;
  }

  public Date getCirculationItemSecondOverDate() {
    return circulationItemSecondOverDate;
  }

  public void setCirculationItemSecondOverDate(Date date) {
    circulationItemSecondOverDate = date;
  }

  public Date getCirculationItemThirdOverDate() {
    return circulationItemThirdOverDate;
  }

  public void setCirculationItemThirdOverDate(Date date) {
    circulationItemThirdOverDate = date;
  }

  public Character getCirculationItemTranstationIndex() {
    return circulationItemTranstationIndex;
  }

  public void setCirculationItemTranstationIndex(Character c) {
    circulationItemTranstationIndex = c;
  }

  public short getCirculationStatesticsTypeCode() {
    return circulationStatesticsTypeCode;
  }

  public void setCirculationStatesticsTypeCode(short s) {
    circulationStatesticsTypeCode = s;
  }

  public int getCopyIdNumber() {
    return copyIdNumber;
  }

  public void setCopyIdNumber(int i) {
    copyIdNumber = i;
  }

  public int getOrganisationNumber() {
    return organisationNumber;
  }

  public void setOrganisationNumber(int i) {
    organisationNumber = i;
  }

  public int getPersonNumber() {
    return personNumber;
  }

  public void setPersonNumber(int i) {
    personNumber = i;
  }

}
