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
public class Globe extends PhysicalDescription {
  private char colourCode;
  private char physicalMediumCode;
  private char typeOfReproductionCode;
  private Character obsolete1;
  private Character obsolete2;

  public Globe() {
    super();
    setHeaderType(23);
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
        + getPhysicalMediumCode()
        + getTypeOfReproductionCode();
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
   */
  @Override
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("X1", session));
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isGlobe()
   */
  @Override
  public boolean isGlobe() {
    return true;
  }


  public char getColourCode() {
    return colourCode;
  }


  public void setColourCode(char c) {
    colourCode = c;
  }


  public Character getObsolete1() {
    return obsolete1;
  }


  public void setObsolete1(Character character) {
    obsolete1 = character;
  }


  public Character getObsolete2() {
    return obsolete2;
  }


  public void setObsolete2(Character character) {
    obsolete2 = character;
  }


  public char getPhysicalMediumCode() {
    return physicalMediumCode;
  }


  public void setPhysicalMediumCode(char c) {
    physicalMediumCode = c;
  }


  public char getTypeOfReproductionCode() {
    return typeOfReproductionCode;
  }


  public void setTypeOfReproductionCode(char c) {
    typeOfReproductionCode = c;
  }


  //@paulm, us_bbl_loading
  @Override
  public void setContentFromMarcString(final String s) {
    setGeneralMaterialDesignationCode(s.charAt(0));
    if (s.length() > 1) setSpecificMaterialDesignationCode(s.charAt(1));
    else setSpecificMaterialDesignationCode('u');
    if (s.length() > 3) setColourCode(s.charAt(3));
    if (s.length() > 4) setPhysicalMediumCode(s.charAt(4));
    if (s.length() > 5) setTypeOfReproductionCode(s.charAt(5));
  }
}
