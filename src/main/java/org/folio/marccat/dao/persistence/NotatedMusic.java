package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.dao.SystemNextNumberDAO;

/**
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class NotatedMusic extends PhysicalDescription {

  public NotatedMusic() {
    super();
    setHeaderType(48);
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
  public void generateNewKey(final Session session) throws HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("XG", session));
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isNotatedMusic()
   */
  @Override
  public boolean isNotatedMusic() {
    return true;
  }



  //@paulm, us_bbl_loading
  @Override
  public void setContentFromMarcString(final String s) {
    setGeneralMaterialDesignationCode(s.charAt(0));
    if (s.length() > 1) setSpecificMaterialDesignationCode(s.charAt(1));
    else setSpecificMaterialDesignationCode('u');
  }

}
