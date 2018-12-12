package org.folio.marccat.dao.persistence;

import org.folio.marccat.exception.DataAccessException;

/**
 * Superclass for single table Codetables (short code)
 *
 * @author paulm
 * @version $Revision: 1.7 $, $Date: 2006/01/11 13:36:22 $
 * @since 1.0
 */
public abstract class T_SINGLE extends CodeTable {
  private int code;

  public int getCode() {
    return code;
  }

  public void setCode(int s) {
    code = s;
  }

  public String getCodeString() {
    return String.valueOf(code);
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof T_SINGLE)) {
      return false;
    }
    return (((T_SINGLE) obj).getCode() == getCode()) && (((T_SINGLE) obj).getLanguage().equals(getLanguage()));
  }

  public int hashCode() {
    return getCode() + getLanguage().hashCode();
  }

  public void setExternalCode(Object extCode) {
    if (extCode instanceof String) {
      if (((String) extCode).trim().length() > 0) {
        code = Short.parseShort((String) extCode);
      } else {
        code = 1;
      }

    } else if (extCode instanceof Short) {
      code = ((Short) extCode).shortValue();
    }
  }

  public int getNextNumber() throws DataAccessException {
    return 0;
  }
}
