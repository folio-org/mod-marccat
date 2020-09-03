package org.folio.marccat.dao.persistence;

/**
  * Superclass for single LONGCHAR Codetables (string code)
  *
  * @author paulm
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
    return getCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj != null && !(obj.getClass().equals(this.getClass())))
      return false;
    return (obj != null && ((T_SINGLE_LONGCHAR) obj).getCode().equals(getCode()))
      && (((T_SINGLE_LONGCHAR) obj).getLanguage().equals(getLanguage()));
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return getCode().hashCode() + getLanguage().hashCode();
  }



  public int getNextNumber() {
    return 0;
  }
}
