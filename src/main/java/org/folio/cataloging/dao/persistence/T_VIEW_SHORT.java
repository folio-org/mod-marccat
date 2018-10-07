/*
 * (c) LibriCore
 *
 * Created on Dec 8, 2004
 *
 * T_VIEW_SHORT.java
 */
package org.folio.cataloging.dao.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.DAOCodeTable;

import java.util.Locale;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/12/14 10:40:41 $
 * @since 1.0
 */
public class T_VIEW_SHORT extends CodeTable {
  private static final Log logger = LogFactory.getLog (T_VIEW_SHORT.class);
  private static final DAOCodeTable daoCodeTable = new DAOCodeTable ( );
  private short code;
  private long translationKey;

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public T_VIEW_SHORT() {
    super ( );
    // TODO Auto-generated constructor stub
  }

  /**
   * @since 1.0
   */
  public short getCode() {
    return code;
  }

  /**
   * @since 1.0
   */
  public void setCode(short s) {
    code = s;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object arg0) {
    if (arg0.getClass ( ).equals (this.getClass ( ))) {
      T_VIEW_SHORT t = (T_VIEW_SHORT) arg0;
      return t.getCode ( ) == this.getCode ( );
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return getCode ( );
  }

  /* (non-Javadoc)
   * @see CodeTable#getCodeString()
   */
  public String getCodeString() {
    return String.valueOf (getCode ( ));
  }

  /* (non-Javadoc)
   * @see CodeTable#getLongText(java.util.Locale)
   */
  public String getLongText(Locale locale) {
    try {
      return daoCodeTable.getTranslationString (getTranslationKey ( ), locale);
    } catch (DataAccessException e) {
//TODO we catch a Data exception here for convenience
// since only this class does db activity to get text values		
      logger.warn ("Data Exception reading translations");
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
      code = Short.parseShort ((String) extCode);
    } else if (extCode instanceof Short) {
      code = ((Short) extCode).shortValue ( );
    }
  }

  public int getNextNumber() throws DataAccessException {
    // TODO Auto-generated method stub
    return 0;
  }
}
