/*
 * (c) LibriCore
 *
 * Created on 02-nov-2004
 *
 * LIB_HRS_OPRTN.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Maite
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class LIB_HRS_OPRTN implements Serializable {
  private int organisationNumber;
  private Date mondayHoursOpeningTime;
  private Date mondayHoursClosingTime;
  private Date tuesdayHoursOpeningTime;
  private Date tuesdayHoursClosingTime;
  private Date wednesdayHoursOpeningTime;
  private Date wednesdayHoursClosingTime;
  private Date thursdayHoursOpeningTime;
  private Date thursdayHoursClosingTime;
  private Date fridayHoursOpeningTime;
  private Date fridayHoursClosingTime;
  private Date saturdayHoursOpeningTime;
  private Date saturdayHoursClosingTime;
  private Date sundayHoursOpeningTime;
  private Date sundayHoursClosingTime;
  private char generalIndicator;

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Date getFridayHoursClosingTime() {
    return fridayHoursClosingTime;
  }

  /**
   * @param date
   * @throws
   * @since 1.0
   */
  public void setFridayHoursClosingTime(Date date) {
    fridayHoursClosingTime = date;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Date getFridayHoursOpeningTime() {
    return fridayHoursOpeningTime;
  }

  /**
   * @param date
   * @throws
   * @since 1.0
   */
  public void setFridayHoursOpeningTime(Date date) {
    fridayHoursOpeningTime = date;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Date getMondayHoursClosingTime() {
    return mondayHoursClosingTime;
  }

  /**
   * @param date
   * @throws
   * @since 1.0
   */
  public void setMondayHoursClosingTime(Date date) {
    mondayHoursClosingTime = date;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Date getMondayHoursOpeningTime() {
    return mondayHoursOpeningTime;
  }

  /**
   * @param date
   * @throws
   * @since 1.0
   */
  public void setMondayHoursOpeningTime(Date date) {
    mondayHoursOpeningTime = date;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public int getOrganisationNumber() {
    return organisationNumber;
  }

  /**
   * @param i
   * @throws
   * @since 1.0
   */
  public void setOrganisationNumber(int i) {
    organisationNumber = i;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Date getSaturdayHoursClosingTime() {
    return saturdayHoursClosingTime;
  }

  /**
   * @param date
   * @throws
   * @since 1.0
   */
  public void setSaturdayHoursClosingTime(Date date) {
    saturdayHoursClosingTime = date;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Date getSaturdayHoursOpeningTime() {
    return saturdayHoursOpeningTime;
  }

  /**
   * @param date
   * @throws
   * @since 1.0
   */
  public void setSaturdayHoursOpeningTime(Date date) {
    saturdayHoursOpeningTime = date;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Date getSundayHoursClosingTime() {
    return sundayHoursClosingTime;
  }

  /**
   * @param date
   * @throws
   * @since 1.0
   */
  public void setSundayHoursClosingTime(Date date) {
    sundayHoursClosingTime = date;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Date getSundayHoursOpeningTime() {
    return sundayHoursOpeningTime;
  }

  /**
   * @param date
   * @throws
   * @since 1.0
   */
  public void setSundayHoursOpeningTime(Date date) {
    sundayHoursOpeningTime = date;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Date getThursdayHoursClosingTime() {
    return thursdayHoursClosingTime;
  }

  /**
   * @param date
   * @throws
   * @since 1.0
   */
  public void setThursdayHoursClosingTime(Date date) {
    thursdayHoursClosingTime = date;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Date getThursdayHoursOpeningTime() {
    return thursdayHoursOpeningTime;
  }

  /**
   * @param date
   * @throws
   * @since 1.0
   */
  public void setThursdayHoursOpeningTime(Date date) {
    thursdayHoursOpeningTime = date;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Date getTuesdayHoursClosingTime() {
    return tuesdayHoursClosingTime;
  }

  /**
   * @param date
   * @throws
   * @since 1.0
   */
  public void setTuesdayHoursClosingTime(Date date) {
    tuesdayHoursClosingTime = date;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Date getTuesdayHoursOpeningTime() {
    return tuesdayHoursOpeningTime;
  }

  /**
   * @param date
   * @throws
   * @since 1.0
   */
  public void setTuesdayHoursOpeningTime(Date date) {
    tuesdayHoursOpeningTime = date;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Date getWednesdayHoursClosingTime() {
    return wednesdayHoursClosingTime;
  }

  /**
   * @param date
   * @throws
   * @since 1.0
   */
  public void setWednesdayHoursClosingTime(Date date) {
    wednesdayHoursClosingTime = date;
  }

  /**
   * @return
   * @throws
   * @since 1.0
   */
  public Date getWednesdayHoursOpeningTime() {
    return wednesdayHoursOpeningTime;
  }

  /**
   * @param date
   * @throws
   * @since 1.0
   */
  public void setWednesdayHoursOpeningTime(Date date) {
    wednesdayHoursOpeningTime = date;
  }


  /**
   * @return Returns the generalIndicator.
   * @throws
   * @since 1.0
   */
  public char getGeneralIndicator() {
    return generalIndicator;
  }

  /**
   * @param generalIndicator The generalIndicator to set.
   * @throws
   * @since 1.0
   */
  public void setGeneralIndicator(char generalIndicator) {
    this.generalIndicator = generalIndicator;
  }
}
