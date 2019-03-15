/*
 * (c) LibriCore
 *
 * Created on 17-ago-2004
 *
 * T_SINGLE_INT.java
 */
package org.folio.marccat.dao.persistence;

import org.folio.marccat.exception.DataAccessException;


/**
 * @author Maite
 * @version $Revision: 1.5 $, $Date: 2005/12/21 13:33:33 $
 * @since 1.0
 */
public class T_SINGLE_INT extends CodeTable {
  private short code;

  public short getCode() {
    return code;
  }


  private void setCode(short s) {
    code = s;
  }


  /* (non-Javadoc)
   * @see CodeTable#getCodeString()
   */
  public String getCodeString() {
    return String.valueOf(code);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof T_SINGLE_INT))
      return false;
    return (((T_SINGLE_INT) obj).getCode() == getCode())
      && (((T_SINGLE_INT) obj).getLanguage().equals(getLanguage()));
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return getCode() + getLanguage().hashCode();
  }

  public void setExternalCode(Object extCode) {
    if (extCode instanceof String) {
      code = Short.parseShort((String) extCode);
    } else if (extCode instanceof Short) {
      code = ((Short) extCode).shortValue();
    }
  }

  public int getNextNumber() throws DataAccessException {
    // TODO Auto-generated method stub
    return 0;
  }
}
