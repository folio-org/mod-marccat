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
  private char obsolete1 = 'u';


  public ElectronicResource() {
    super();
    setHeaderType(42);
  }

  @Override
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

  @Override
  public void generateNewKey(final Session session) throws HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setKeyNumber(dao.getNextNumber("XA", session));
  }

  @Override
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
