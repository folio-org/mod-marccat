package org.folio.marccat.dao.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.dao.CodeTableDAO;
import org.folio.marccat.exception.DataAccessException;

import java.util.Locale;

/**
 * @author paulm
 * @since 1.0
 */
public class T_VIEW_LONGCHAR extends CodeTable {
  private static final Log logger = LogFactory.getLog(T_VIEW_LONGCHAR.class);
  private static final CodeTableDAO CODE_TABLE_DAO = new CodeTableDAO();
  private String code;
  private long translationKey;


  public T_VIEW_LONGCHAR() {
    super();

  }


  public String getCode() {
    return code;
  }


  public void setCode(String s) {
    code = s;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object arg0) {
    if (arg0 != null &&  arg0.getClass().equals(this.getClass())) {
      T_VIEW_LONGCHAR t = (T_VIEW_LONGCHAR) arg0;
      return t.getCode().equals(this.getCode());
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
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
      return CODE_TABLE_DAO.getTranslationString(getTranslationKey(), locale);
    } catch (DataAccessException e) {
      logger.warn("Data Exception reading translations");
      return null;
    }
  }


  public long getTranslationKey() {
    return translationKey;
  }


  public void setTranslationKey(long i) {
    translationKey = i;
  }


  public int getNextNumber() throws DataAccessException {

    return 0;
  }

}
