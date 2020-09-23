package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.exception.DataAccessException;


/**
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class Text extends PhysicalDescription {

  public Text() {
    super();
    setHeaderType(44);
  }

  /* (non-Javadoc)
   * @see FixedField#getDisplayString()
   */
  @Override
  public String getDisplayString() {
    return
      ""
        + getGeneralMaterialDesignationCode()
        + getSpecificMaterialDesignationCode();
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
   */
  @Override
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("XC", session));
  }


  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isText()
   */
  @Override
  public boolean isText() {
    return true;
  }


  @Override
  public void setContentFromMarcString(final String s) {
    setGeneralMaterialDesignationCode(s.charAt(0));
    if (s.length() > 1) {
      setSpecificMaterialDesignationCode(s.charAt(1));
    } else {
      setSpecificMaterialDesignationCode('u');
    }

  }

}
