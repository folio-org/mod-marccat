package org.folio.marccat.dao.persistence;

import org.folio.marccat.exception.DataAccessException;


/**
 * Superclass for single char Codetables (char code)
 *
 * @author paulm
 * @since 1.0
 */
public class T_SINGLE_CHAR extends CodeTable {

  private char code;

  public char getCode() {
    return code;
  }

  public void setCode(char c) {
    code = c;
  }

  /* (non-Javadoc)
   * @see CodeTable#getCodeString()
   */
  public String getCodeString() {
    return String.valueOf(code);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj != null && !(obj.getClass().equals(this.getClass())))
      return false;
    return (obj != null && ((T_SINGLE_CHAR) obj).getCode() == getCode())
      && (((T_SINGLE_CHAR) obj).getLanguage().equals(getLanguage()));
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
