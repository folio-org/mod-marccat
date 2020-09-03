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
public class TactileMaterial extends PhysicalDescription {
  private String classOfBrailleWritingCodes;
  private char levelOfContractionCode;
  private String brailleMusicFormatCodes;
  private char specificPhysicalCharacteristicsCode;

  public TactileMaterial() {
    super();
    setHeaderType(46);
  }

  @Override
  public String getDisplayString() {
    return EMPTY_STRING
      + getGeneralMaterialDesignationCode()
      + getSpecificMaterialDesignationCode()
      + " "
      + getClassOfBrailleWritingCodes()
      + getLevelOfContractionCode()
      + getBrailleMusicFormatCodes()
      + getSpecificPhysicalCharacteristicsCode();
  }

  @Override
  public void generateNewKey(final Session session) throws HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("XE", session));
  }

  @Override
  public boolean isTactileMaterial() {
    return true;
  }


  public String getBrailleMusicFormatCodes() {
    return brailleMusicFormatCodes;
  }


  public void setBrailleMusicFormatCodes(String string) {
    brailleMusicFormatCodes = string;
  }

  public char[] getBrailleMusicFormatChar() {
    if (brailleMusicFormatCodes == null) {
      return new char[0];
    } else {
      return brailleMusicFormatCodes.toCharArray();
    }
  }


  public String getClassOfBrailleWritingCodes() {
    return classOfBrailleWritingCodes;
  }


  public void setClassOfBrailleWritingCodes(String string) {
    classOfBrailleWritingCodes = string;
  }

  public char[] getClassOfBrailleWritingChar() {
    if (classOfBrailleWritingCodes == null) {
      return new char[0];
    } else {
      return classOfBrailleWritingCodes.toCharArray();
    }
  }


  public char getLevelOfContractionCode() {
    return levelOfContractionCode;
  }


  public void setLevelOfContractionCode(char c) {
    levelOfContractionCode = c;
  }


  public char getSpecificPhysicalCharacteristicsCode() {
    return specificPhysicalCharacteristicsCode;
  }


  public void setSpecificPhysicalCharacteristicsCode(char c) {
    specificPhysicalCharacteristicsCode = c;
  }


  @Override
  public void setContentFromMarcString(final String s) {
    setGeneralMaterialDesignationCode(s.charAt(0));
    if (s.length() > 1) setSpecificMaterialDesignationCode(s.charAt(1));
    else setSpecificMaterialDesignationCode('u');
    if (s.length() > 4) setClassOfBrailleWritingCodes(s.substring(3, 5));
    if (s.length() > 5) setLevelOfContractionCode(s.charAt(5));
    if (s.length() > 8) setBrailleMusicFormatCodes(s.substring(6, 9));
    if (s.length() > 9) setSpecificPhysicalCharacteristicsCode(s.charAt(9));
  }
}
