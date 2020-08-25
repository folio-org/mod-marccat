package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.dao.SystemNextNumberDAO;
import static org.folio.marccat.config.constants.Global.EMPTY_STRING;

/**
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class Kit extends PhysicalDescription {

  public Kit() {
    super();
    setHeaderType(47);
  }

  @Override
  public String getDisplayString() {
    return EMPTY_STRING
      + getGeneralMaterialDesignationCode()
      + getSpecificMaterialDesignationCode();
  }

  @Override
  public void generateNewKey(final Session session) throws HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("XF", session));
  }

  @Override
  public boolean isKit() {
    return true;
  }


  @Override
  public void setContentFromMarcString(final String s) {
    setGeneralMaterialDesignationCode(s.charAt(0));
    if (s.length() > 1) setSpecificMaterialDesignationCode(s.charAt(1));
    else setSpecificMaterialDesignationCode('u');
  }
}
