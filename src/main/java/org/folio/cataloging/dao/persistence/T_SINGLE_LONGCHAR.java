/*
 * (c) LibriCore
 *
 * Created on 11-ago-2004
 *
 * T_SINGLE_LONGCHAR.java
 */
package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.common.DataAccessException;


/**
 * @author Maite
 * @version $Revision: 1.6 $, $Date: 2007/05/04 08:14:58 $
 * @since 1.0
 */
public class T_SINGLE_LONGCHAR extends CodeTable {

  private String code;

  public String getCode() {
    return code;
  }

  public void setCode(String string) {
    code = string;
  }

  /* (non-Javadoc)
   * @see CodeTable#getCodeString()
   */
  public String getCodeString() {
    return code;
  }

  public boolean equals(Object obj) {
    if (!(obj.getClass ( ).equals (this.getClass ( ))))
      return false;
    return (((T_SINGLE_LONGCHAR) obj).getCode ( ).equals (getCode ( )))
      && (((T_SINGLE_LONGCHAR) obj).getLanguage ( ).equals (getLanguage ( )));
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return getCode ( ).hashCode ( ) + getLanguage ( ).hashCode ( );
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
