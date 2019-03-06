package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.exception.DataAccessException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.folio.marccat.config.log.Global.EMPTY_STRING;

/**
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class ElectronicResource extends PhysicalDescription {
  private char colourCode;
  private char dimensionsCode;
  private char includesSoundCode;
  private String imageBitDepth;
  private char fileFormatsCode;
  private char qualityAssuranceTargetCode;
  private char antecedentSourceCode;
  private char levelOfCompressionCode;
  private char reformattingQualityCode;
  private char obsolete1;


  public ElectronicResource() {
    super();
    setHeaderType(42);
  }

  public String getDisplayString() {
    return EMPTY_STRING
        + getGeneralMaterialDesignationCode()
        + getSpecificMaterialDesignationCode()
        + " "
        + getColourCode()
        + getDimensionsCode()
        + getIncludesSoundCode()
        + getImageBitDepth()
        + getFileFormatsCode()
        + getQualityAssuranceTargetCode()
        + getAntecedentSourceCode()
        + getLevelOfCompressionCode()
        + getReformattingQualityCode();

  }

  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("XA", session));
  }

  public boolean isElectronicResource() {
    return true;
  }


  public char getAntecedentSourceCode() {
    return antecedentSourceCode;
  }


  public void setAntecedentSourceCode(char c) {
    antecedentSourceCode = c;
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


  public char getFileFormatsCode() {
    return fileFormatsCode;
  }


  public void setFileFormatsCode(char c) {
    fileFormatsCode = c;
  }


  public String getImageBitDepth() {
    return imageBitDepth;
  }


  public void setImageBitDepth(String string) {
    imageBitDepth = string;
  }


  public char getIncludesSoundCode() {
    return includesSoundCode;
  }


  public void setIncludesSoundCode(char c) {
    includesSoundCode = c;
  }


  public char getLevelOfCompressionCode() {
    return levelOfCompressionCode;
  }


  public void setLevelOfCompressionCode(char c) {
    levelOfCompressionCode = c;
  }


  public char getObsolete1() {
    return obsolete1;
  }


  public void setObsolete1(char c) {
    obsolete1 = c;
  }


  public char getQualityAssuranceTargetCode() {
    return qualityAssuranceTargetCode;
  }


  public void setQualityAssuranceTargetCode(char c) {
    qualityAssuranceTargetCode = c;
  }


  public char getReformattingQualityCode() {
    return reformattingQualityCode;
  }


  public void setReformattingQualityCode(char c) {
    reformattingQualityCode = c;
  }

  public Element generateModelXmlElementContent(Document xmlDocument) {
    Element content = null;
    if (xmlDocument != null) {
      content = xmlDocument.createElement("content");
      content.setAttribute("generalMaterialDesignationCode", EMPTY_STRING + getGeneralMaterialDesignationCode());
      content.setAttribute("specificMaterialDesignationCode", EMPTY_STRING + getSpecificMaterialDesignationCode());
      content.setAttribute("colourCode", EMPTY_STRING + getColourCode());
      content.setAttribute("dimensionsCode", EMPTY_STRING + getDimensionsCode());
      content.setAttribute("includesSoundCode", EMPTY_STRING + getIncludesSoundCode());
      content.setAttribute("imageBitDepth", getImageBitDepth());
      content.setAttribute("fileFormatsCode", EMPTY_STRING + getFileFormatsCode());
      content.setAttribute("qualityAssuranceTargetCode", EMPTY_STRING + getQualityAssuranceTargetCode());
      content.setAttribute("antecedentSourceCode", EMPTY_STRING + getAntecedentSourceCode());
      content.setAttribute("levelOfCompressionCode", EMPTY_STRING + getLevelOfCompressionCode());
      content.setAttribute("reformattingQualityCode", EMPTY_STRING + getReformattingQualityCode());
    }
    return content;
  }

  public void parseModelXmlElementContent(Element xmlElement) {
    Element content = (Element) xmlElement.getChildNodes().item(0);
    setGeneralMaterialDesignationCode(content.getAttribute("generalMaterialDesignationCode").charAt(0));
    setSpecificMaterialDesignationCode(content.getAttribute("specificMaterialDesignationCode").charAt(0));
    setColourCode(content.getAttribute("colourCode").charAt(0));
    setDimensionsCode(content.getAttribute("dimensionsCode").charAt(0));
    setIncludesSoundCode(content.getAttribute("includesSoundCode").charAt(0));
    setImageBitDepth(content.getAttribute("imageBitDepth"));
    setFileFormatsCode(content.getAttribute("fileFormatsCode").charAt(0));
    setQualityAssuranceTargetCode(content.getAttribute("qualityAssuranceTargetCode").charAt(0));
    setAntecedentSourceCode(content.getAttribute("antecedentSourceCode").charAt(0));
    setLevelOfCompressionCode(content.getAttribute("levelOfCompressionCode").charAt(0));
    setReformattingQualityCode(content.getAttribute("reformattingQualityCode").charAt(0));
  }

  @Override
  public void setContentFromMarcString(final String content) {
    setGeneralMaterialDesignationCode(content.charAt(0));
    if (content.length() > 1) setSpecificMaterialDesignationCode(content.charAt(1));
    else setSpecificMaterialDesignationCode('u');
    if (content.length() > 3) setColourCode(content.charAt(3));
    if (content.length() > 4) setDimensionsCode(content.charAt(4));
    if (content.length() > 5) setIncludesSoundCode(content.charAt(5));
    if (content.length() > 9) setImageBitDepth(content.substring(6, 9));
    if (content.length() > 9) setFileFormatsCode(content.charAt(9));
    if (content.length() > 10) setQualityAssuranceTargetCode(content.charAt(10));
    if (content.length() > 11) setAntecedentSourceCode(content.charAt(11));
    if (content.length() > 12) setLevelOfCompressionCode(content.charAt(12));
    if (content.length() > 13) setReformattingQualityCode(content.charAt(13));
  }
}
