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
public class ProjectedGraphic extends PhysicalDescription {
  private char colourCode;
  private char baseOfEmulsionCode;
  private char soundOnMediumOrSeparateCode;
  private char mediumForSoundCode;
  private char dimensionsCode;
  private char secondarySupportMaterialCode;
  private char obsolete1;

  public ProjectedGraphic() {
    super();
    setHeaderType(28);
  }


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

  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("X7", session));
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isProjectedGraphic()
   */
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

  public Element generateModelXmlElementContent(Document xmlDocument) {
    Element content = null;
    if (xmlDocument != null) {
      content = xmlDocument.createElement("content");
      content.setAttribute("generalMaterialDesignationCode", EMPTY_STRING + getGeneralMaterialDesignationCode());
      content.setAttribute("specificMaterialDesignationCode", EMPTY_STRING + getSpecificMaterialDesignationCode());
      content.setAttribute("colourCode", EMPTY_STRING + getColourCode());
      content.setAttribute("baseOfEmulsionCode", EMPTY_STRING + getBaseOfEmulsionCode());
      content.setAttribute("soundOnMediumOrSeparateCode", EMPTY_STRING + getSoundOnMediumOrSeparateCode());
      content.setAttribute("mediumForSoundCode", EMPTY_STRING + getMediumForSoundCode());
      content.setAttribute("dimensionsCode", EMPTY_STRING + getDimensionsCode());
      content.setAttribute("secondarySupportMaterialCode", EMPTY_STRING + getSecondarySupportMaterialCode());
    }
    return content;
  }

  public void parseModelXmlElementContent(Element xmlElement) {
    Element content = (Element) xmlElement.getChildNodes().item(0);
    setGeneralMaterialDesignationCode(content.getAttribute("generalMaterialDesignationCode").charAt(0));
    setSpecificMaterialDesignationCode(content.getAttribute("specificMaterialDesignationCode").charAt(0));
    setColourCode(content.getAttribute("colourCode").charAt(0));
    setBaseOfEmulsionCode(content.getAttribute("baseOfEmulsionCode").charAt(0));
    setSoundOnMediumOrSeparateCode(content.getAttribute("soundOnMediumOrSeparateCode").charAt(0));
    setMediumForSoundCode(content.getAttribute("mediumForSoundCode").charAt(0));
    setDimensionsCode(content.getAttribute("dimensionsCode").charAt(0));
    setSecondarySupportMaterialCode(content.getAttribute("secondarySupportMaterialCode").charAt(0));
  }

  @Override
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
