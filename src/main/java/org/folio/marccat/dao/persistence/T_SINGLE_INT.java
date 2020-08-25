package org.folio.marccat.dao.persistence;

import org.folio.marccat.exception.DataAccessException;


/**
  * Superclass for single int Codetables (int code)
  *
  * @author paulm
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


  public int getNextNumber() throws DataAccessException {

    return 0;
  }
}
