/*
 * (c) LibriCore
 *
 * Created on Aug 31, 2006
 *
 * T_ITM_DSPLY_FRMT.java
 */
package org.folio.cataloging.dao.persistence;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/08/31 12:13:01 $
 * @since 1.0
 */
public abstract class T_ITM_DSPLY_FRMT extends T_SINGLE {
  public static final short LABELLED = 1;
  public static final short MARC = 2;
  protected String stylesheetPropertyKey;

  /**
   * @since 1.0
   */
  public String getStylesheetPropertyKey() {
    return stylesheetPropertyKey;
  }

  /**
   * @since 1.0
   */
  public void setStylesheetPropertyKey(String string) {
    stylesheetPropertyKey = string;
  }

}
