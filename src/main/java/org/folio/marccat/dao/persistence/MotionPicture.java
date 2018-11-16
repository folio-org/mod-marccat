package org.folio.marccat.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
  private char obsolete1;

  public MotionPicture() {
    super();
    setHeaderType(26);
  }

  /* (non-Javadoc)
   * @see FixedField#getDisplayString()
   */
  public String getDisplayString() {
    String result =
      ""
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
    return result;
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.Tag#generateNewKey()
   */
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("X4", session));
  }

  /* (non-Javadoc)
   * @see librisuite.business.cataloguing.bibliographic.PhysicalDescription#isMotionPicture()
   */
  public boolean isMotionPicture() {
    return true;
  }

  /**
   * @since 1.0
   */
  public char getBaseOfFilmCode() {
    return baseOfFilmCode;
  }

  /**
   * @since 1.0
   */
  public void setBaseOfFilmCode(char c) {
    baseOfFilmCode = c;
  }

  /**
   * @since 1.0
   */
  public char getColourCode() {
    return colourCode;
  }

  /**
   * @since 1.0
   */
  public void setColourCode(char c) {
    colourCode = c;
  }

  /**
   * @since 1.0
   */
  public char getCompletenessCode() {
    return completenessCode;
  }

  /**
   * @since 1.0
   */
  public void setCompletenessCode(char c) {
    completenessCode = c;
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
  public char getDeteriorationStageCode() {
    return deteriorationStageCode;
  }

  /**
   * @since 1.0
   */
  public void setDeteriorationStageCode(char c) {
    deteriorationStageCode = c;
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
  public char getGenerationCode() {
    return generationCode;
  }

  /**
   * @since 1.0
   */
  public void setGenerationCode(char c) {
    generationCode = c;
  }

  /**
   * @since 1.0
   */
  public char getIncludesSoundCode() {
    return includesSoundCode;
  }

  /**
   * @since 1.0
   */
  public void setIncludesSoundCode(char c) {
    includesSoundCode = c;
  }

  /**
   * @since 1.0
   */
  public String getInspectionDate() {
    return inspectionDate;
  }

  /**
   * @since 1.0
   */
  public void setInspectionDate(String string) {
    inspectionDate = string;
  }

  /**
   * @since 1.0
   */
  public char getKindOfColourStockCode() {
    return kindOfColourStockCode;
  }

  /**
   * @since 1.0
   */
  public void setKindOfColourStockCode(char c) {
    kindOfColourStockCode = c;
  }

  /**
   * @since 1.0
   */
  public char getMediumForSoundCode() {
    return mediumForSoundCode;
  }

  /**
   * @since 1.0
   */
  public void setMediumForSoundCode(char c) {
    mediumForSoundCode = c;
  }

  /**
   * @since 1.0
   */
  public char getPolarityCode() {
    return polarityCode;
  }

  /**
   * @since 1.0
   */
  public void setPolarityCode(char c) {
    polarityCode = c;
  }

  /**
   * @since 1.0
   */
  public char getPresentationFormatCode() {
    return presentationFormatCode;
  }

  /**
   * @since 1.0
   */
  public void setPresentationFormatCode(char c) {
    presentationFormatCode = c;
  }

  /**
   * @since 1.0
   */
  public char getProductionElementsCode() {
    return productionElementsCode;
  }

  /**
   * @since 1.0
   */
  public void setProductionElementsCode(char c) {
    productionElementsCode = c;
  }

  /**
   * @since 1.0
   */
  public char getRefinedCategoriesOfColourCode() {
    return refinedCategoriesOfColourCode;
  }

  /**
   * @since 1.0
   */
  public void setRefinedCategoriesOfColourCode(char c) {
    refinedCategoriesOfColourCode = c;
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

  public Element generateModelXmlElementContent(Document xmlDocument) {
    Element content = null;
    if (xmlDocument != null) {
      content = xmlDocument.createElement("content");
      content.setAttribute("generalMaterialDesignationCode", "" + getGeneralMaterialDesignationCode());
      content.setAttribute("specificMaterialDesignationCode", "" + getSpecificMaterialDesignationCode());
      content.setAttribute("colourCode", "" + getColourCode());
      content.setAttribute("presentationFormatCode", "" + getPresentationFormatCode());
      content.setAttribute("includesSoundCode", "" + getIncludesSoundCode());
      content.setAttribute("mediumForSoundCode", "" + getMediumForSoundCode());
      content.setAttribute("dimensionsCode", "" + getDimensionsCode());
      content.setAttribute("configurationCode", "" + getConfigurationCode());
      content.setAttribute("productionElementsCode", "" + getProductionElementsCode());
      content.setAttribute("polarityCode", "" + getPolarityCode());
      content.setAttribute("generationCode", "" + getGenerationCode());
      content.setAttribute("baseOfFilmCode", "" + getBaseOfFilmCode());
      content.setAttribute("refinedCategoriesOfColourCode", "" + getRefinedCategoriesOfColourCode());
      content.setAttribute("kindOfColourStockCode", "" + getKindOfColourStockCode());
      content.setAttribute("deteriorationStageCode", "" + getDeteriorationStageCode());
      content.setAttribute("completenessCode", "" + getCompletenessCode());
      content.setAttribute("inspectionDate", "" + getInspectionDate());
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

  //@paulm, us_bbl_loading
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
