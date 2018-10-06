/*
 * (c) LibriCore
 *
 * Created on 26-jul-2004
 *
 * CIRTN_HLD.java
 */
package org.folio.cataloging.dao.persistence;

import java.util.Date;

/**
 * @author elena
 * @version $Revision: 1.3 $, $Date: 2004/08/04 09:02:38 $
 * @since 1.0
 */
public class CIRTN_HLD {

  private int copyIdNumber;
  private int organisationNumber;
  private int bibItemNumber;
  private int personNumber;
  private int circulationHoldingLocationOrganisationNumber;
  private short circulationHoldingTypeCode;
  private Date circulationHoldingCreationDate;
  private Date circulationHolginShelfDate;
  private Date timeHoldingStratmentDate;
  private Date timeHoldingEndDate;
  private Character timeHoldingStatusIndex;
  private Character circulationHoldingPrintIndex;
  private int branchOrganisationNumber;

  public int getBibItemNumber() {
    return bibItemNumber;
  }

  public void setBibItemNumber(int i) {
    bibItemNumber = i;
  }

  public int getBranchOrganisationNumber() {
    return branchOrganisationNumber;
  }

  public void setBranchOrganisationNumber(int i) {
    branchOrganisationNumber = i;
  }

  public Date getCirculationHoldingCreationDate() {
    return circulationHoldingCreationDate;
  }

  public void setCirculationHoldingCreationDate(Date date) {
    circulationHoldingCreationDate = date;
  }

  public int getCirculationHoldingLocationOrganisationNumber() {
    return circulationHoldingLocationOrganisationNumber;
  }

  public void setCirculationHoldingLocationOrganisationNumber(int i) {
    circulationHoldingLocationOrganisationNumber = i;
  }

  public Character getCirculationHoldingPrintIndex() {
    return circulationHoldingPrintIndex;
  }

  public void setCirculationHoldingPrintIndex(Character character) {
    circulationHoldingPrintIndex = character;
  }

  public short getCirculationHoldingTypeCode() {
    return circulationHoldingTypeCode;
  }

  public void setCirculationHoldingTypeCode(short s) {
    circulationHoldingTypeCode = s;
  }

  public Date getCirculationHolginShelfDate() {
    return circulationHolginShelfDate;
  }

  public void setCirculationHolginShelfDate(Date date) {
    circulationHolginShelfDate = date;
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

  public Date getTimeHoldingEndDate() {
    return timeHoldingEndDate;
  }

  public void setTimeHoldingEndDate(Date date) {
    timeHoldingEndDate = date;
  }

  public Character getTimeHoldingStatusIndex() {
    return timeHoldingStatusIndex;
  }

  public void setTimeHoldingStatusIndex(Character character) {
    timeHoldingStatusIndex = character;
  }

  public Date getTimeHoldingStratmentDate() {
    return timeHoldingStratmentDate;
  }

  public void setTimeHoldingStratmentDate(Date date) {
    timeHoldingStratmentDate = date;
  }

}
