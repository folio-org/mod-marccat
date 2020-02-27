package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.exception.DataAccessException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.folio.marccat.config.constants.Global.EMPTY_STRING;

/**
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class MotionPicture extends PhysicalDescription {
  private char colourCode;
  private char presentationFormatCode;
  private char includesSoundCode;
  private char mediumForSoundCode;
  private char dimensionsCode;
  private char configurationCode;
  private char productionElementsCode;
  private char polarityCode;
  private char generationCode;
  private char baseOfFilmCode;
  private char refinedCategoriesOfColourCode;
  private char kindOfColourStockCode;
  private char deteriorationStageCode;
  private char completenessCode;
  private String inspectionDate;
  private char obsolete1 = 'u';

  public MotionPicture() {
    super();
    setHeaderType(26);
  }

  /* (non-Javadoc)
   * @see FixedField#getDisplayString()
   */
  @Override
  public String getDisplayString() {
    return EMPTY_STRING
      + getGeneralMaterialDesignationCode()
      + getSpecificMaterialDesignationCode()
      + " "
      + getColourCode()
      + getPresentationFormatCode()
      + getIncludesSoundCode()
      + getMediumForSoundCode()
      + getDimensionsCode()
      + getConfigurationCode()
      + getProductionElementsCode()
      + getPolarityCode()
      + getGenerationCode()
      + getBaseOfFilmCode()
      + getRefinedCategoriesOfColourCode()
      + getKindOfColourStockCode()
      + getDeteriorationStageCode()
      + getCompletenessCode()
      + getInspectionDate();
  }

  @Override
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("X4", session));
  }

  @Override
  public boolean isMotionPicture() {
    return true;
  }


  public char getBaseOfFilmCode() {
    return baseOfFilmCode;
  }


  public void setBaseOfFilmCode(char c) {
    baseOfFilmCode = c;
  }


  public char getColourCode() {
    return colourCode;
  }


  public void setColourCode(char c) {
    colourCode = c;
  }


  public char getCompletenessCode() {
    return completenessCode;
  }


  public void setCompletenessCode(char c) {
    completenessCode = c;
  }


  public char getConfigurationCode() {
    return configurationCode;
  }


  public void setConfigurationCode(char c) {
    configurationCode = c;
  }


  public char getDeteriorationStageCode() {
    return deteriorationStageCode;
  }


  public void setDeteriorationStageCode(char c) {
    deteriorationStageCode = c;
  }


  public char getDimensionsCode() {
    return dimensionsCode;
  }


  public void setDimensionsCode(char c) {
    dimensionsCode = c;
  }


  public char getGenerationCode() {
    return generationCode;
  }


  public void setGenerationCode(char c) {
    generationCode = c;
  }


  public char getIncludesSoundCode() {
    return includesSoundCode;
  }


  public void setIncludesSoundCode(char c) {
    includesSoundCode = c;
  }


  public String getInspectionDate() {
    return inspectionDate;
  }


  public void setInspectionDate(String string) {
    inspectionDate = string;
  }


  public char getKindOfColourStockCode() {
    return kindOfColourStockCode;
  }


  public void setKindOfColourStockCode(char c) {
    kindOfColourStockCode = c;
  }


  public char getMediumForSoundCode() {
    return mediumForSoundCode;
  }


  public void setMediumForSoundCode(char c) {
    mediumForSoundCode = c;
  }


  public char getPolarityCode() {
    return polarityCode;
  }


  public void setPolarityCode(char c) {
    polarityCode = c;
  }


  public char getPresentationFormatCode() {
    return presentationFormatCode;
  }


  public void setPresentationFormatCode(char c) {
    presentationFormatCode = c;
  }


  public char getProductionElementsCode() {
    return productionElementsCode;
  }


  public void setProductionElementsCode(char c) {
    productionElementsCode = c;
  }


  public char getRefinedCategoriesOfColourCode() {
    return refinedCategoriesOfColourCode;
  }


  public void setRefinedCategoriesOfColourCode(char c) {
    refinedCategoriesOfColourCode = c;
  }


  public char getObsolete1() {
    return obsolete1;
  }


  public void setObsolete1(char c) {
    obsolete1 = c;
  }

  public Element generateModelXmlElementContent(Document xmlDocument) {
    Element content = null;
    if (xmlDocument != null) {
      content = xmlDocument.createElement("content");
      content.setAttribute("generalMaterialDesignationCode", EMPTY_STRING + getGeneralMaterialDesignationCode());
      content.setAttribute("specificMaterialDesignationCode", EMPTY_STRING + getSpecificMaterialDesignationCode());
      content.setAttribute("colourCode", EMPTY_STRING + getColourCode());
      content.setAttribute("presentationFormatCode", EMPTY_STRING + getPresentationFormatCode());
      content.setAttribute("includesSoundCode", EMPTY_STRING + getIncludesSoundCode());
      content.setAttribute("mediumForSoundCode", EMPTY_STRING + getMediumForSoundCode());
      content.setAttribute("dimensionsCode", EMPTY_STRING + getDimensionsCode());
      content.setAttribute("configurationCode", EMPTY_STRING + getConfigurationCode());
      content.setAttribute("productionElementsCode", EMPTY_STRING + getProductionElementsCode());
      content.setAttribute("polarityCode", EMPTY_STRING + getPolarityCode());
      content.setAttribute("generationCode", EMPTY_STRING + getGenerationCode());
      content.setAttribute("baseOfFilmCode", EMPTY_STRING + getBaseOfFilmCode());
      content.setAttribute("refinedCategoriesOfColourCode", EMPTY_STRING + getRefinedCategoriesOfColourCode());
      content.setAttribute("kindOfColourStockCode", EMPTY_STRING + getKindOfColourStockCode());
      content.setAttribute("deteriorationStageCode", EMPTY_STRING + getDeteriorationStageCode());
      content.setAttribute("completenessCode", EMPTY_STRING + getCompletenessCode());
      content.setAttribute("inspectionDate", EMPTY_STRING + getInspectionDate());
    }
    return content;
  }

  public void parseModelXmlElementContent(Element xmlElement) {
    Element content = (Element) xmlElement.getChildNodes().item(0);
    setGeneralMaterialDesignationCode(content.getAttribute("generalMaterialDesignationCode").charAt(0));
    setSpecificMaterialDesignationCode(content.getAttribute("specificMaterialDesignationCode").charAt(0));
    setColourCode(content.getAttribute("colourCode").charAt(0));
    setPresentationFormatCode(content.getAttribute("presentationFormatCode").charAt(0));
    setIncludesSoundCode(content.getAttribute("includesSoundCode").charAt(0));
    setMediumForSoundCode(content.getAttribute("mediumForSoundCode").charAt(0));
    setDimensionsCode(content.getAttribute("dimensionsCode").charAt(0));
    setConfigurationCode(content.getAttribute("configurationCode").charAt(0));
    setProductionElementsCode(content.getAttribute("productionElementsCode").charAt(0));
    setPolarityCode(content.getAttribute("polarityCode").charAt(0));
    setGenerationCode(content.getAttribute("generationCode").charAt(0));
    setBaseOfFilmCode(content.getAttribute("baseOfFilmCode").charAt(0));
    setRefinedCategoriesOfColourCode(content.getAttribute("refinedCategoriesOfColourCode").charAt(0));
    setKindOfColourStockCode(content.getAttribute("kindOfColourStockCode").charAt(0));
    setDeteriorationStageCode(content.getAttribute("deteriorationStageCode").charAt(0));
    setCompletenessCode(content.getAttribute("completenessCode").charAt(0));
    setInspectionDate(content.getAttribute("inspectionDate"));
  }

  @Override
  public void setContentFromMarcString(final String s) {
    setGeneralMaterialDesignationCode(s.charAt(0));
    if (s.length() > 1) setSpecificMaterialDesignationCode(s.charAt(1));
    else setSpecificMaterialDesignationCode('u');
    if (s.length() > 3) setColourCode(s.charAt(3));
    if (s.length() > 4) setPresentationFormatCode(s.charAt(4));
    if (s.length() > 5) setIncludesSoundCode(s.charAt(5));
    if (s.length() > 6) setMediumForSoundCode(s.charAt(6));
    if (s.length() > 7) setDimensionsCode(s.charAt(7));
    if (s.length() > 8) setConfigurationCode(s.charAt(8));
    if (s.length() > 9) setProductionElementsCode(s.charAt(9));
    if (s.length() > 10) setPolarityCode(s.charAt(10));
    if (s.length() > 11) setGenerationCode(s.charAt(11));
    if (s.length() > 12) setBaseOfFilmCode(s.charAt(12));
    if (s.length() > 13) setRefinedCategoriesOfColourCode(s.charAt(13));
    if (s.length() > 14) setKindOfColourStockCode(s.charAt(14));
    if (s.length() > 15) setDeteriorationStageCode(s.charAt(15));
    if (s.length() > 16) setCompletenessCode(s.charAt(16));
    if (s.length() > 22) setInspectionDate(s.substring(17, 23));
  }
}
