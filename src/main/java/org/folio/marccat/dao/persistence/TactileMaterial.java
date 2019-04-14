package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

  public Element generateModelXmlElementContent(Document xmlDocument) {
    Element content = null;
    if (xmlDocument != null) {
      content = xmlDocument.createElement("content");
      content.setAttribute("generalMaterialDesignationCode", EMPTY_STRING + getGeneralMaterialDesignationCode());
      content.setAttribute("specificMaterialDesignationCode", EMPTY_STRING + getSpecificMaterialDesignationCode());
      content.setAttribute("classOfBrailleWritingCodes", EMPTY_STRING + getClassOfBrailleWritingCodes());
      content.setAttribute("levelOfContractionCode", EMPTY_STRING + getLevelOfContractionCode());
      content.setAttribute("brailleMusicFormatCodes", EMPTY_STRING + getBrailleMusicFormatCodes());
      content.setAttribute("specificPhysicalCharacteristicsCode", EMPTY_STRING + getSpecificPhysicalCharacteristicsCode());
    }
    return content;
  }

  public void parseModelXmlElementContent(Element xmlElement) {
    Element content = (Element) xmlElement.getChildNodes().item(0);
    setGeneralMaterialDesignationCode(content.getAttribute("generalMaterialDesignationCode").charAt(0));
    setSpecificMaterialDesignationCode(content.getAttribute("specificMaterialDesignationCode").charAt(0));
    setClassOfBrailleWritingCodes(content.getAttribute("classOfBrailleWritingCodes"));
    setLevelOfContractionCode(content.getAttribute("levelOfContractionCode").charAt(0));
    setBrailleMusicFormatCodes(content.getAttribute("brailleMusicFormatCodes"));
    setSpecificPhysicalCharacteristicsCode(content.getAttribute("specificPhysicalCharacteristicsCode").charAt(0));
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
