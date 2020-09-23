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
public class NonProjectedGraphic extends PhysicalDescription {
  private char colourCode;
  private char primarySupportMaterialCode;
  private char secondarySupportMaterialCode;
  private char obsolete1 = 'u';

  public NonProjectedGraphic() {
    super();
    setHeaderType(27);
  }

  /* (non-Javadoc)
   * @see FixedField#getDisplayString()
   */
  @Override
  public String getDisplayString() {
    return
      ""
        + getGeneralMaterialDesignationCode()
        + getSpecificMaterialDesignationCode()
        + " "
        + getColourCode()
        + getPrimarySupportMaterialCode()
        + getSecondarySupportMaterialCode();
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
   */
  @Override
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("X6", session));
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isNonProjectedGraphic()
   */
  @Override
  public boolean isNonProjectedGraphic() {
    return true;
  }


  public char getColourCode() {
    return colourCode;
  }


  public void setColourCode(char c) {
    colourCode = c;
  }


  public char getPrimarySupportMaterialCode() {
    return primarySupportMaterialCode;
  }


  public void setPrimarySupportMaterialCode(char c) {
    primarySupportMaterialCode = c;
  }


  public char getSecondarySupportMaterialCode() {
    return secondarySupportMaterialCode;
  }


  public void setSecondarySupportMaterialCode(char c) {
    secondarySupportMaterialCode = c;
  }


  public char getObsolete1() {
    return obsolete1;
  }


  public void setObsolete1(char c) {
    obsolete1 = c;
  }


  //@paulm, us_bbl_loading
  @Override
  public void setContentFromMarcString(final String s) {
    setGeneralMaterialDesignationCode(s.charAt(0));
    if (s.length() > 1) setSpecificMaterialDesignationCode(s.charAt(1));
    else setSpecificMaterialDesignationCode('u');
    if (s.length() > 3) setColourCode(s.charAt(3));
    if (s.length() > 4) setPrimarySupportMaterialCode(s.charAt(4));
    if (s.length() > 5) setSecondarySupportMaterialCode(s.charAt(5));
  }

}
