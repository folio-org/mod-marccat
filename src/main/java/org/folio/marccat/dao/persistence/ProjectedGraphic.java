package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.exception.DataAccessException;
import static org.folio.marccat.config.constants.Global.EMPTY_STRING;

/**
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class ProjectedGraphic extends PhysicalDescription {
  private char colourCode;
  private char baseOfEmulsionCode;
  private char soundOnMediumOrSeparateCode;
  private char mediumForSoundCode;
  private char dimensionsCode;
  private char secondarySupportMaterialCode;
  private char obsolete1 = 'u';

  public ProjectedGraphic() {
    super();
    setHeaderType(28);
  }


  @Override
  public String getDisplayString() {
    return EMPTY_STRING
      + getGeneralMaterialDesignationCode()
      + getSpecificMaterialDesignationCode()
      + " "
      + getColourCode()
      + getBaseOfEmulsionCode()
      + getSoundOnMediumOrSeparateCode()
      + getMediumForSoundCode()
      + getDimensionsCode()
      + getSecondarySupportMaterialCode();
  }

  @Override
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("X7", session));
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isProjectedGraphic()
   */
  @Override
  public boolean isProjectedGraphic() {
    return true;
  }


  public char getBaseOfEmulsionCode() {
    return baseOfEmulsionCode;
  }


  public void setBaseOfEmulsionCode(char c) {
    baseOfEmulsionCode = c;
  }


  public char getColourCode() {
    return colourCode;
  }


  public void setColourCode(char c) {
    colourCode = c;
  }


  public char getDimensionsCode() {
    return dimensionsCode;
  }


  public void setDimensionsCode(char c) {
    dimensionsCode = c;
  }


  public char getMediumForSoundCode() {
    return mediumForSoundCode;
  }


  public void setMediumForSoundCode(char c) {
    mediumForSoundCode = c;
  }


  public char getObsolete1() {
    return obsolete1;
  }


  public void setObsolete1(char c) {
    obsolete1 = c;
  }


  public char getSecondarySupportMaterialCode() {
    return secondarySupportMaterialCode;
  }


  public void setSecondarySupportMaterialCode(char c) {
    secondarySupportMaterialCode = c;
  }


  public char getSoundOnMediumOrSeparateCode() {
    return soundOnMediumOrSeparateCode;
  }


  public void setSoundOnMediumOrSeparateCode(char c) {
    soundOnMediumOrSeparateCode = c;
  }


  public void setContentFromMarcString(final String s) {
    setGeneralMaterialDesignationCode(s.charAt(0));
    if (s.length() > 1) setSpecificMaterialDesignationCode(s.charAt(1));
    else setSpecificMaterialDesignationCode('u');
    if (s.length() > 3) setColourCode(s.charAt(3));
    if (s.length() > 4) setBaseOfEmulsionCode(s.charAt(4));
    if (s.length() > 5) setSoundOnMediumOrSeparateCode(s.charAt(5));
    if (s.length() > 6) setMediumForSoundCode(s.charAt(6));
    if (s.length() > 7) setDimensionsCode(s.charAt(7));
    if (s.length() > 8) setSecondarySupportMaterialCode(s.charAt(8));
  }
}
