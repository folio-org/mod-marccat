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
public class VideoRecording extends PhysicalDescription {
  private char colourCode;
  private char formatCode;
  private char includesSoundCode;
  private char mediumForSoundCode;
  private char dimensionsCode;
  private char configurationCode;
  private char obsolete1 = 'u';

  public VideoRecording() {
    super();
    setHeaderType(30);
  }

  @Override
  public String getDisplayString() {
    return EMPTY_STRING
      + getGeneralMaterialDesignationCode()
      + getSpecificMaterialDesignationCode()
      + " "
      + getColourCode()
      + getFormatCode()
      + getIncludesSoundCode()
      + getMediumForSoundCode()
      + getDimensionsCode()
      + getConfigurationCode();
  }

  @Override
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("X9", session));
  }

  @Override
  public boolean isVideoRecording() {
    return true;
  }


  public char getColourCode() {
    return colourCode;
  }


  public void setColourCode(char c) {
    colourCode = c;
  }


  public char getConfigurationCode() {
    return configurationCode;
  }


  public void setConfigurationCode(char c) {
    configurationCode = c;
  }


  public char getDimensionsCode() {
    return dimensionsCode;
  }


  public void setDimensionsCode(char c) {
    dimensionsCode = c;
  }


  public char getFormatCode() {
    return formatCode;
  }


  public void setFormatCode(char c) {
    formatCode = c;
  }


  public char getIncludesSoundCode() {
    return includesSoundCode;
  }


  public void setIncludesSoundCode(char c) {
    includesSoundCode = c;
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



  @Override
  public void setContentFromMarcString(final String s) {
    setGeneralMaterialDesignationCode(s.charAt(0));
    if (s.length() > 1) setSpecificMaterialDesignationCode(s.charAt(1));
    else setSpecificMaterialDesignationCode('u');
    if (s.length() > 3) setColourCode(s.charAt(3));
    if (s.length() > 4) setFormatCode(s.charAt(4));
    if (s.length() > 5) setIncludesSoundCode(s.charAt(5));
    if (s.length() > 6) setMediumForSoundCode(s.charAt(6));
    if (s.length() > 7) setDimensionsCode(s.charAt(7));
    if (s.length() > 8) setConfigurationCode(s.charAt(8));
  }
}
