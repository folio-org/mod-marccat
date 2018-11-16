/*
 * (c) LibriCore
 *
 * Created on Dec 8, 2004
 *
 * T_VIEW_LONGCHAR.java
 */
package org.folio.marccat.dao.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.dao.DAOCodeTable;
import org.folio.marccat.exception.DataAccessException;

import java.util.Locale;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/02/02 14:09:42 $
 * @since 1.0
 */
public class T_VIEW_LONGCHAR extends CodeTable {
  private static final Log logger = LogFactory.getLog(T_VIEW_LONGCHAR.class);
  private static final DAOCodeTable daoCodeTable = new DAOCodeTable();
  private String code;
  private long translationKey;

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public T_VIEW_LONGCHAR() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * @since 1.0
   */
  public String getCode() {
    return code;
  }

  /**
   * @since 1.0
   */
  public void setCode(String s) {
    code = s;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object arg0) {
    if (arg0.getClass().equals(this.getClass())) {
      T_VIEW_LONGCHAR t = (T_VIEW_LONGCHAR) arg0;
      return t.getCode().equals(this.getCode());
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return getCode().hashCode();
  }

  /* (non-Javadoc)
   * @see CodeTable#getCodeString()
   */
  public String getCodeString() {
    return String.valueOf(getCode());
  }

  /* (non-Javadoc)
   * @see CodeTable#getLongText(java.util.Locale)
   */
  public String getLongText(Locale locale) {
    try {
      return daoCodeTable.getTranslationString(getTranslationKey(), locale);
    } catch (DataAccessException e) {
//TODO we catch a Data exception here for convenience
// since only this class does db activity to get text values		
      logger.warn("Data Exception reading translations");
      return null;
    }
  }

  /**
   * @since 1.0
   */
  public long getTranslationKey() {
    return translationKey;
  }

  /**
   * @since 1.0
   */
  public void setTranslationKey(long i) {
    translationKey = i;
  }

  public void setExternalCode(Object extCode) {
    if (extCode instanceof String) {
      code = (String) extCode;
    }
  }

  public int getNextNumber() throws DataAccessException {
    // TODO Auto-generated method stub
    return 0;
  }

}
