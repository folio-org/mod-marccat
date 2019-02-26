package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.exception.DataAccessException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.folio.marccat.config.Global.EMPTY_STRING;

/**
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class SoundRecording extends PhysicalDescription {
  private char speedCode;
  private char configurationCode;
  private char grooveWidthCode;
  private char dimensionsCode;
  private char tapeWidthCode;
  private char tapeConfigurationCode;
  private char discTypeCode;
  private char sndMaterialTypeCode;
  private char cuttingTypeCode;
  private char specialPlaybackCharacteristicsCode;
  private char storageTechniqueCode;
  private char obsolete1;


  public SoundRecording() {
    super();
    setHeaderType(29);
  }

  /* (non-Javadoc)
   * @see FixedField#getDisplayString()
   */
  public String getDisplayString() {
    String result =
      EMPTY_STRING
        + getGeneralMaterialDesignationCode()
        + getSpecificMaterialDesignationCode()
        + " "
        + getSpeedCode()
        + getConfigurationCode()
        + getGrooveWidthCode()
        + getDimensionsCode()
        + getTapeWidthCode()
        + getTapeConfigurationCode()
        + getDiscTypeCode()
        + getSndMaterialTypeCode()
        + getCuttingTypeCode()
        + getSpecialPlaybackCharacteristicsCode()
        + getStorageTechniqueCode();
    return result;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
   */
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("X8", session));
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isSoundRecording()
   */
  public boolean isSoundRecording() {
    return true;
  }

  /**
   * @since 1.0
   */
  public char getConfigurationCode() {
    return configurationCode;
  }

  /**
   * @since 1.0
   */
  public void setConfigurationCode(char c) {
    configurationCode = c;
  }

  /**
   * @since 1.0
   */
  public char getCuttingTypeCode() {
    return cuttingTypeCode;
  }

  /**
   * @since 1.0
   */
  public void setCuttingTypeCode(char c) {
    cuttingTypeCode = c;
  }

  /**
   * @since 1.0
   */
  public char getDimensionsCode() {
    return dimensionsCode;
  }

  /**
   * @since 1.0
   */
  public void setDimensionsCode(char c) {
    dimensionsCode = c;
  }

  /**
   * @since 1.0
   */
  public char getDiscTypeCode() {
    return discTypeCode;
  }

  /**
   * @since 1.0
   */
  public void setDiscTypeCode(char c) {
    discTypeCode = c;
  }

  /**
   * @since 1.0
   */
  public char getGrooveWidthCode() {
    return grooveWidthCode;
  }

  /**
   * @since 1.0
   */
  public void setGrooveWidthCode(char c) {
    grooveWidthCode = c;
  }

  /**
   * @since 1.0
   */
  public char getSndMaterialTypeCode() {
    return sndMaterialTypeCode;
  }

  /**
   * @since 1.0
   */
  public void setSndMaterialTypeCode(char c) {
    sndMaterialTypeCode = c;
  }

  /**
   * @since 1.0
   */
  public char getObsolete1() {
    return obsolete1;
  }

  /**
   * @since 1.0
   */
  public void setObsolete1(char c) {
    obsolete1 = c;
  }

  /**
   * @since 1.0
   */
  public char getSpecialPlaybackCharacteristicsCode() {
    return specialPlaybackCharacteristicsCode;
  }

  /**
   * @since 1.0
   */
  public void setSpecialPlaybackCharacteristicsCode(char c) {
    specialPlaybackCharacteristicsCode = c;
  }

  /**
   * @since 1.0
   */
  public char getSpeedCode() {
    return speedCode;
  }

  /**
   * @since 1.0
   */
  public void setSpeedCode(char c) {
    speedCode = c;
  }

  /**
   * @since 1.0
   */
  public char getStorageTechniqueCode() {
    return storageTechniqueCode;
  }

  /**
   * @since 1.0
   */
  public void setStorageTechniqueCode(char c) {
    storageTechniqueCode = c;
  }

  /**
   * @since 1.0
   */
  public char getTapeConfigurationCode() {
    return tapeConfigurationCode;
  }

  /**
   * @since 1.0
   */
  public void setTapeConfigurationCode(char c) {
    tapeConfigurationCode = c;
  }

  /**
   * @since 1.0
   */
  public char getTapeWidthCode() {
    return tapeWidthCode;
  }

  /**
   * @since 1.0
   */
  public void setTapeWidthCode(char c) {
    tapeWidthCode = c;
  }

  public Element generateModelXmlElementContent(Document xmlDocument) {
    Element content = null;
    if (xmlDocument != null) {
      content = xmlDocument.createElement("content");
      content.setAttribute("generalMaterialDesignationCode", EMPTY_STRING + getGeneralMaterialDesignationCode());
      content.setAttribute("specificMaterialDesignationCode", EMPTY_STRING + getSpecificMaterialDesignationCode());
      content.setAttribute("speedCode", EMPTY_STRING + getSpeedCode());
      content.setAttribute("configurationCode", EMPTY_STRING + getConfigurationCode());
      content.setAttribute("grooveWidthCode", EMPTY_STRING + getGrooveWidthCode());
      content.setAttribute("dimensionsCode", EMPTY_STRING + getDimensionsCode());
      content.setAttribute("tapeWidthCode", EMPTY_STRING + getTapeWidthCode());
      content.setAttribute("tapeConfigurationCode", EMPTY_STRING + getTapeConfigurationCode());
      content.setAttribute("discTypeCode", EMPTY_STRING + getDiscTypeCode());
      content.setAttribute("sndMaterialTypeCode", EMPTY_STRING + getSndMaterialTypeCode());
      content.setAttribute("cuttingTypeCode", EMPTY_STRING + getCuttingTypeCode());
      content.setAttribute("specialPlaybackCharacteristicsCode", EMPTY_STRING + getSpecialPlaybackCharacteristicsCode());
      content.setAttribute("storageTechniqueCode", EMPTY_STRING + getStorageTechniqueCode());
    }
    return content;
  }

  public void parseModelXmlElementContent(Element xmlElement) {
    Element content = (Element) xmlElement.getChildNodes().item(0);
    setGeneralMaterialDesignationCode(content.getAttribute("generalMaterialDesignationCode").charAt(0));
    setSpecificMaterialDesignationCode(content.getAttribute("specificMaterialDesignationCode").charAt(0));
    setSpeedCode(content.getAttribute("speedCode").charAt(0));
    setConfigurationCode(content.getAttribute("configurationCode").charAt(0));
    setGrooveWidthCode(content.getAttribute("grooveWidthCode").charAt(0));
    setDimensionsCode(content.getAttribute("dimensionsCode").charAt(0));
    setTapeWidthCode(content.getAttribute("tapeWidthCode").charAt(0));
    setTapeConfigurationCode(content.getAttribute("tapeConfigurationCode").charAt(0));
    setDiscTypeCode(content.getAttribute("discTypeCode").charAt(0));
    setSndMaterialTypeCode(content.getAttribute("sndMaterialTypeCode").charAt(0));
    setCuttingTypeCode(content.getAttribute("cuttingTypeCode").charAt(0));
    setSpecialPlaybackCharacteristicsCode(content.getAttribute("specialPlaybackCharacteristicsCode").charAt(0));
    setStorageTechniqueCode(content.getAttribute("storageTechniqueCode").charAt(0));
  }

  //@paulm, us_bbl_loading
  @Override
  public void setContentFromMarcString(final String s) {
    setGeneralMaterialDesignationCode(s.charAt(0));
    if (s.length() > 1) setSpecificMaterialDesignationCode(s.charAt(1));
    else setSpecificMaterialDesignationCode('u');
    if (s.length() > 3) setSpeedCode(s.charAt(3));
    if (s.length() > 4) setConfigurationCode(s.charAt(4));
    if (s.length() > 5) setGrooveWidthCode(s.charAt(5));
    if (s.length() > 6) setDimensionsCode(s.charAt(6));
    if (s.length() > 7) setTapeWidthCode(s.charAt(7));
    if (s.length() > 8) setTapeConfigurationCode(s.charAt(8));
    if (s.length() > 9) setDiscTypeCode(s.charAt(9));
    if (s.length() > 10) setSndMaterialTypeCode(s.charAt(10));
    if (s.length() > 11) setCuttingTypeCode(s.charAt(11));
    if (s.length() > 12) setSpecialPlaybackCharacteristicsCode(s.charAt(12));
    if (s.length() > 13) setStorageTechniqueCode(s.charAt(13));
  }
}
