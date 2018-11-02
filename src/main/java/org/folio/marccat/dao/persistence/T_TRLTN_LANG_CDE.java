/*
 * (c) LibriCore
 *
 * Created on Jun 24, 2004
 *
 * T_TRLTN_LANG_CDE.java
 */
package org.folio.marccat.dao.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2006/03/21 15:08:48 $
 * @since 1.0
 */
public class T_TRLTN_LANG_CDE implements Serializable {
  private int code;
  private int sequence;
  private boolean obsoleteIndicator;
  private Date activeStartDate;
  private Date activeEndDate;
  private String text;
  private String comment;

  /**
   * Getter for activeEndDate
   *
   * @return activeEndDate
   */
  public Date getActiveEndDate() {
    return activeEndDate;
  }

  /**
   * Setter for activeEndDate
   *
   * @param date activeEndDate
   */
  public void setActiveEndDate(Date date) {
    activeEndDate = date;
  }

  /**
   * Getter for activeStartDate
   *
   * @return activeStartDate
   */
  public Date getActiveStartDate() {
    return activeStartDate;
  }

  /**
   * Setter for activeStartDate
   *
   * @param date activeStartDate
   */
  public void setActiveStartDate(Date date) {
    activeStartDate = date;
  }

  /**
   * Getter for code
   *
   * @return code
   */
  public int getCode() {
    return code;
  }

  /**
   * Setter for code
   *
   * @param s code
   */
  public void setCode(int s) {
    code = s;
  }

  /**
   * Getter for comment
   *
   * @return comment
   */
  public String getComment() {
    return comment;
  }

  /**
   * Setter for comment
   *
   * @param string comment
   */
  public void setComment(String string) {
    comment = string;
  }

  /**
   * Getter for obsoleteIndicator
   *
   * @return obsoleteIndicator
   */
  public boolean isObsoleteIndicator() {
    return obsoleteIndicator;
  }

  /**
   * Setter for obosleteIndicator
   *
   * @param b obosleteIndicator
   */
  public void setObsoleteIndicator(boolean b) {
    obsoleteIndicator = b;
  }

  /**
   * Getter for sequence
   *
   * @return sequence
   */
  public int getSequence() {
    return sequence;
  }

  /**
   * Setter for sequence
   *
   * @param s sequence
   */
  public void setSequence(int s) {
    sequence = s;
  }

  /**
   * Getter for text
   *
   * @return text
   */
  public String getText() {
    return text;
  }

  /**
   * Setter for text
   *
   * @param string text
   */
  public void setText(String string) {
    text = string;
  }

}
