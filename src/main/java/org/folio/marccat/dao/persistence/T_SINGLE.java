package org.folio.marccat.dao.persistence;

import org.folio.marccat.exception.DataAccessException;

/**
 * Superclass for single table Codetables (short code)
 *
 * @author paulm
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

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof T_SINGLE)) {
      return false;
    }
    return (((T_SINGLE) obj).getCode() == getCode()) && (((T_SINGLE) obj).getLanguage().equals(getLanguage()));
  }

  @Override
  public int hashCode() {
    return getCode() + getLanguage().hashCode();
  }



  public int getNextNumber() throws DataAccessException {
    return 0;
  }
}
