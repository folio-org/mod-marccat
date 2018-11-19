package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.bibliographic.BibliographicAccessPoint;
import org.folio.marccat.business.cataloguing.common.OrderedTag;
import org.folio.marccat.config.GlobalStorage;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static java.util.Optional.ofNullable;

/**
 * Persistent class for  CNTL_NBR_ACS_PNT.
 *
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class ControlNumberAccessPoint extends BibliographicAccessPoint implements OrderedTag {

  private CNTL_NBR descriptor = new CNTL_NBR();
  private char validationCode = 'a';
  private Integer sequenceNumber;

  public ControlNumberAccessPoint() {
    super();
  }

  public ControlNumberAccessPoint(final int itemNbr) {
    super(itemNbr);
  }

  /**
   * Gets descriptor associated to control number access point.
   *
   * @return descriptor.
   */
  public Descriptor getDescriptor() {
    return descriptor;
  }

  /**
   * Sets descriptor to control number access point.
   *
   * @param descriptor -- the descriptor associated to control number access point.
   */
  public void setDescriptor(final Descriptor descriptor) {
    this.descriptor = (CNTL_NBR) descriptor;
  }

  /**
   * Gets stringText for visualization mode with sub-field codes.
   *
   * @return stringText.
   */
  public StringText getStringText() {
    final StringText s = super.getStringText();
    if (getValidationCode() != 'a') {
      s.getSubfield(0).setCode(String.valueOf(getValidationCode()));
    }
    return s;
  }

  /**
   * Sets descriptor string text.
   *
   * @param stringText -- the string text to set.
   */
  public void setDescriptorStringText(final StringText stringText) {
    descriptor.setStringText(stringText.toString());
  }

  /**
   * Gets the stringText associated to control number access point.
   *
   * @return stringText.
   */
  public StringText getAccessPointStringText() {
    return new StringText();
  }

  /**
   * Sets stringText to control number access point.
   *
   * @param stringText -- the stringText to set.
   */
  public void setAccessPointStringText(final StringText stringText) {
  }

  /**
   * Gets control number marc category code.
   *
   * @return category.
   */
  public int getCategory() {
    return GlobalStorage.CONTROL_NUMBER_CATEGORY;
  }

  /**
   * Gets correlation values of control number descriptor.
   *
   * @return correlationValues.
   */
  public CorrelationValues getCorrelationValues() {
    return getDescriptor().getCorrelationValues().change(2, getFunctionCode());
  }

  /**
   * Sets correlation values to control number descriptor.
   *
   * @param v -- the correlation values to set.
   */
  public void setCorrelationValues(final CorrelationValues v) {
    setFunctionCode(v.getValue(2));
    getDescriptor().setCorrelationValues(v);
  }

  /**
   * Checks correlation key value is changed for control number access point.
   *
   * @param v -- the correlation values.
   * @return boolean.
   */
  public boolean correlationChangeAffectsKey(final CorrelationValues v) {
    return (v.isValueDefined(2)) && (v.getValue(2) != getFunctionCode());
  }

  /**
   * Gets permission string to compare with authorization agent.
   *
   * @return "editControlNumber".
   */
  public String getRequiredEditPermission() {
    return GlobalStorage.CNTL_NBR_REQUIRED_PERMISSION;
  }

  /**
   * Gets validation subfield code.
   *
   * @return validationCode.
   */
  public char getValidationCode() {
    return validationCode;
  }

  /**
   * Sets validation code to control number access point.
   *
   * @param c -- the validation code to set.
   */
  public void setValidationCode(char c) {
    validationCode = c;
  }

  /**
   * Generates an element content from document.
   *
   * @param xmlDocument -- the xml document.
   * @return an element content.
   */
  public Element generateModelXmlElementContent(final Document xmlDocument) {
    return ofNullable(xmlDocument).map(content -> {
      Element element = getStringText().generateModelXmlElementContent(xmlDocument);
      return element;
    }).orElse(null);
  }

  /**
   * Compares an object with another one.
   *
   * @param obj -- the object to compare.
   * @return true if equals.
   */
  public boolean equals(final Object obj) {
    if (!(obj instanceof ControlNumberAccessPoint))
      return false;

    ControlNumberAccessPoint other = (ControlNumberAccessPoint) obj;
    return super.equals(obj) && (other.functionCode == this.functionCode) && (other.descriptor.getKey().getHeadingNumber() == this.descriptor.getKey().getHeadingNumber());
  }

  public int hashCode() {
    return super.hashCode();
  }

  /**
   * Gets the sequence number associated to control number access point.
   *
   * @return sequenceNumber.
   */
  public Integer getSequenceNumber() {
    return sequenceNumber;
  }

  /**
   * Sets sequence number to title access point.
   *
   * @param seqNumber -- the sequence number to set.
   */
  public void setSequenceNumber(final Integer seqNumber) {
    sequenceNumber = seqNumber;
    super.setSequenceNumber(sequenceNumber);
  }

  //TODO move in storageService and set after creation (as was: called two constructor)
  public void setDefaultFunctionCode() {
		/*
		int funCode=0;
		funCode= new Integer(configHandler.findValue("t_cntl_nbr_typ_fnctn","controlNumberAccessPoint.functionCode"));
		int function= this.getFunction(funCode);
		setFunctionCode((short)function);
		*/
  }
}
