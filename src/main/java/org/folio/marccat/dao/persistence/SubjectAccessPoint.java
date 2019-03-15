package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.bibliographic.BibliographicAccessPoint;
import org.folio.marccat.business.cataloguing.common.OrderedTag;
import org.folio.marccat.config.log.Global;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;


/**
 * Persistent class for SBJCT_ACS_PNT.
 *
 * @author nbianchini
 */
public class SubjectAccessPoint extends BibliographicAccessPoint implements OrderedTag {

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -5339141299630141762L;

  /**
   * The function code.
   */
  private int functionCode = -1;

  /**
   * The work relator code.
   */
  private String workRelatorCode;

  /**
   * The work relator stringtext.
   */
  private String workRelatorStringtext;

  /**
   * The sequence number.
   */
  private int sequenceNumber;

  /**
   * The descriptor.
   */
  private SBJCT_HDG descriptor = new SBJCT_HDG();

  /**
   * Instantiates a new subject access point.
   */
  public SubjectAccessPoint() {
    super();
  }

  /**
   * Instantiates a new subject access point.
   *
   * @param itemNbr the item nbr
   */
  public SubjectAccessPoint(final int itemNbr) {
    super(itemNbr);
  }

  /**
   * Gets the function code.
   *
   * @return the subject access point function code.
   */
  @Override
  public int getFunctionCode() {
    return functionCode;
  }

  /**
   * Sets function code associated to subject access point.
   *
   * @param code -- the function code to set.
   */
  @Override
  public void setFunctionCode(final int code) {
    functionCode = code;
  }

  /**
   * Gets the sequence number associated to access point.
   *
   * @return sequenceNumber.
   */
  public Integer getSequenceNumber() {
    return sequenceNumber;
  }

  /**
   * Sets sequence number to access point.
   *
   * @param sequenceNbr -- the sequence number to set.
   */
  public void setSequenceNumber(final Integer sequenceNbr) {
    sequenceNumber = (sequenceNbr != null) ? sequenceNbr : 0;
    super.setSequenceNumber(sequenceNumber);
  }

  /**
   * Gets name relator code if is a name subject.
   *
   * @return the work relator code.
   */
  public String getWorkRelatorCode() {
    return workRelatorCode;
  }

  /**
   * Sets work relator code to subject.
   *
   * @param relatorCode -- the relator code to set.
   */
  public void setWorkRelatorCode(final String relatorCode) {
    workRelatorCode = relatorCode;
  }

  /**
   * Gets name relator string text if is a name subject.
   *
   * @return the work relator string text.
   */
  public String getWorkRelatorStringtext() {
    return workRelatorStringtext;
  }

  /**
   * Sets work relator string text to subject.
   *
   * @param string -- the relator string to set.
   */
  public void setWorkRelatorStringtext(final String string) {
    workRelatorStringtext = string;
  }

  /**
   * Compares an object with another one.
   *
   * @param obj -- the object to compare.
   * @return true if equals.
   */
  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof SubjectAccessPoint))
      return false;
    final SubjectAccessPoint other = (SubjectAccessPoint) obj;
    return super.equals(obj) && (other.functionCode == this.functionCode);
  }

  /**
   * Hash code.
   *
   * @return hashCode.
   */
  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /**
   * Return descriptor associated to subject access point.
   *
   * @return descriptor.
   */
  public Descriptor getDescriptor() {
    return descriptor;
  }

  /**
   * Sets descriptor.
   *
   * @param sbjct_hdg -- the descriptor associated to classification access point.
   */
  public void setDescriptor(final Descriptor sbjct_hdg) {
    descriptor = (SBJCT_HDG) sbjct_hdg;
  }

  /**
   * Checks correlation key value is changed for subject access point.
   *
   * @param v -- the correlation values.
   * @return boolean.
   */
  @Override
  public boolean correlationChangeAffectsKey(final CorrelationValues v) {
    return (v.isValueDefined(2)) && (v.getValue(2) != getFunctionCode());
  }

  /**
   * Gets permission string to compare with authorization agent.
   *
   * @return "editSubject".
   */
  @Override
  public String getRequiredEditPermission() {
    return Global.SUBJECT_REQUIRED_PERMISSION;
  }

  /**
   * Gets correlation values of subject descriptor.
   *
   * @return correlationValues.
   */
  public CorrelationValues getCorrelationValues() {
    return getDescriptor().getCorrelationValues().change(2, getFunctionCode());
  }

  /**
   * Sets correlation values to subject descriptor.
   *
   * @param v -- the correlation values to set.
   */
  public void setCorrelationValues(final CorrelationValues v) {
    setFunctionCode(v.getValue(2));
    getDescriptor().setCorrelationValues(v);
  }

  /**
   * Gets subject marc category code.
   *
   * @return category.
   */
  @Override
  public int getCategory() {
    return Global.SUBJECT_CATEGORY;
  }

  /**
   * Gets the stringText associated to subject access point.
   *
   * @return stringText.
   */
  public StringText getAccessPointStringText() {
    StringText text = new StringText(workRelatorStringtext);
    text.parse(workRelatorCode);
    return text;
  }

  /**
   * Sets stringText to subject access point.
   *
   * @param stringText -- the stringText to set.
   */
  public void setAccessPointStringText(final StringText stringText) {
    workRelatorCode = stringText.getSubfieldsWithCodes(Global.WORK_REL_SUBFIELD_CODE).toString();
    workRelatorStringtext = stringText.getSubfieldsWithCodes(Global.SUBJECT_WORK_REL_STRING_TEXT_SUBFIELD_CODES).toString();
  }

  /**
   * Sets descriptor string text.
   *
   * @param stringText -- the string text to set.
   */
  public void setDescriptorStringText(final StringText stringText) {
    getDescriptor().setStringText(stringText.getSubfieldsWithoutCodes(Global.SUBJECT_VARIANT_CODES).toString());
  }

  /**
   * Gets the variant codes.
   *
   * @return variant subfield codes.
   */
  @Override
  public String getVariantCodes() {
    return Global.SUBJECT_VARIANT_CODES;
  }

  /**
   * Adds the punctuation.
   *
   * @return the string text
   * @throws Exception the exception
   */
  @Override
  public StringText addPunctuation() throws Exception {
    final StringText result = new StringText(getStringText().toString());
    try {
      final CorrelationKey marc = getMarcEncoding();
      if (marc.getMarcTag().equals("600")) {
        result.addPrecedingPunctuation("d", ",", ",");
      }
      if (Global.SUBJECTS_4.contains(marc.getMarcTag())) {
        result.removePrecedingPunctuation("4", ",");
      }
      if (Global.SUBJECTS_E.contains(marc.getMarcTag())) {
        result.addPrecedingPunctuation("e", ",", ",");
      }
      if (Global.SUBJECTS_4.contains(marc.getMarcTag()) && !result.getSubfieldsWithCodes("t").isEmpty()) {
          result.addTerminalPunctuation("4", ".?!)]-", ".");
        }
      return result;
    } catch (Exception e) {
      return result;
    }
  }


}
