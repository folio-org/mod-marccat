package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.bibliographic.NameTitleComponent;
import org.folio.marccat.business.cataloguing.common.OrderedTag;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.integration.GlobalStorage;
import org.folio.marccat.shared.CorrelationValues;
import org.folio.marccat.util.StringText;

import java.util.List;

/**
 * Persistent class for NME_ACS_PNT.
 *
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */

@SuppressWarnings("unchecked")
public class NameAccessPoint extends NameTitleComponent implements OrderedTag {
  private static final long serialVersionUID = 4936157294497482982L;
  private String workRelatorCode;
  private String institution;
  private String workRelatorStringtext;
  private String otherSubfields;
  private Integer sequenceNumber;
  private NME_HDG descriptor = new NME_HDG();

  public NameAccessPoint() {
    super();
  }

  public NameAccessPoint(final int itemNbr) {
    super(itemNbr);
  }

  /**
   * Gets institution associated to name access point.
   *
   * @return institution.
   */
  public String getInstitution() {
    return institution;
  }

  /**
   * Sets institution to name access point.
   *
   * @param institution -- the institution to set.
   */
  public void setInstitution(final String institution) {
    this.institution = institution;
  }

  /**
   * Gets other sub-fields associated to name access point.
   *
   * @return otherSubfields.
   */
  public String getOtherSubfields() {
    return otherSubfields;
  }

  /**
   * Sets other Subfields to name access point.
   *
   * @param subfields -- the other subfields to set.
   */
  public void setOtherSubfields(final String subfields) {
    otherSubfields = subfields;
  }

  /**
   * Gets the sequence number associated to name access point.
   *
   * @return sequenceNumber.
   */
  public Integer getSequenceNumber() {
    return sequenceNumber;
  }

  /**
   * Sets sequence number to name access point.
   *
   * @param sequenceNbr -- the sequence number to set.
   */
  public void setSequenceNumber(final Integer sequenceNbr) {
    sequenceNumber = sequenceNbr;
    super.setSequenceNumber(sequenceNumber);
  }

  /**
   * Gets work relator code associated to name access point.
   *
   * @return workRelatorCode.
   */
  public String getWorkRelatorCode() {
    return workRelatorCode;
  }

  /**
   * Sets work relator code to name access point.
   *
   * @param relatorCode -- the work relator code to set.
   */
  public void setWorkRelatorCode(final String relatorCode) {
    workRelatorCode = relatorCode;
  }

  /**
   * Gets work relator stringText associated to name access point.
   *
   * @return workRelatorStringtext.
   */
  public String getWorkRelatorStringtext() {
    return workRelatorStringtext;
  }

  /**
   * Sets work relator stringText to name access point.
   *
   * @param relatorStringText -- the work relator stringText to set.
   */
  public void setWorkRelatorStringtext(final String relatorStringText) {
    workRelatorStringtext = relatorStringText;
  }

  /**
   * Compares an object with another one.
   *
   * @param obj -- the object to compare.
   * @return true if equals.
   */
  public boolean equals(final Object obj) {
    if (!(obj instanceof NameAccessPoint))
      return false;
    NameAccessPoint other = (NameAccessPoint) obj;
    return super.equals(obj)
      && (other.getFunctionCode() == this.getFunctionCode())
      && (other.nameTitleHeadingNumber == this.nameTitleHeadingNumber);
  }

  public int hashCode() {
    return this.getItemNumber() + this.getNameTitleHeadingNumber();
  }

  /**
   * Gets descriptor associated to name access point.
   *
   * @return descriptor.
   */
  public Descriptor getDescriptor() {
    return descriptor;
  }

  /**
   * Sets descriptor to name access point.
   *
   * @param nme_hdg -- the descriptor associated to name access point.
   */
  public void setDescriptor(final Descriptor nme_hdg) {
    descriptor = (NME_HDG) nme_hdg;
  }

  /**
   * Checks correlation key value is changed for name access point.
   *
   * @param v -- the correlation values.
   * @return boolean.
   */
  public boolean correlationChangeAffectsKey(final CorrelationValues v) {
    return (v.isValueDefined(3) && (v.getValue(3) != getFunctionCode()));
  }

  /**
   * Gets permission string to compare with authorization agent.
   *
   * @return "editName".
   */
  public String getRequiredEditPermission() {
    return GlobalStorage.NAME_REQUIRED_PERMISSION;
  }

  /**
   * Gets correlation values of name descriptor.
   *
   * @return correlationValues.
   */
  public CorrelationValues getCorrelationValues() {
    return getDescriptor().getCorrelationValues().change(3, getFunctionCode());
  }

  /**
   * Sets correlation values to name descriptor.
   *
   * @param v -- the correlation values to set.
   */
  public void setCorrelationValues(CorrelationValues v) {
    setFunctionCode(v.getValue(3));
    getDescriptor().setCorrelationValues(v);
  }

  /**
   * Gets name marc category code.
   *
   * @return category.
   */
  public int getCategory() {
    return GlobalStorage.NAME_CATEGORY;
  }

  /**
   * Gets the stringText associated to name access point.
   *
   * @return stringText.
   */
  public StringText getAccessPointStringText() {
    final StringText text = new StringText(workRelatorStringtext);
    text.parse(otherSubfields);
    text.parse(workRelatorCode);
    text.parse(institution);
    return text;
  }

  /**
   * Sets stringText to name access point.
   *
   * @param stringText -- the stringText to set.
   */
  public void setAccessPointStringText(final StringText stringText) {
    workRelatorStringtext = stringText.getSubfieldsWithCodes(GlobalStorage.NAME_WORK_REL_STRING_TEXT_SUBFIELD_CODES).toString();
    otherSubfields = stringText.getSubfieldsWithCodes(GlobalStorage.NAME_OTHER_SUBFIELD_CODES).toString();
    workRelatorCode = stringText.getSubfieldsWithCodes(GlobalStorage.WORK_REL_SUBFIELD_CODE).toString();
    institution = stringText.getSubfieldsWithCodes(GlobalStorage.NAME_TITLE_INSTITUTION_SUBFIELD_CODE).toString();
  }

  /**
   * Sets descriptor string text.
   *
   * @param stringText -- the string text to set.
   */
  public void setDescriptorStringText(final StringText stringText) {
    getDescriptor().setStringText(stringText.getSubfieldsWithoutCodes(GlobalStorage.NAME_VARIANT_SUBFIELD_CODES).toString());
  }

  /**
   * Returns key using tag number and second correlation value.
   *
   * @return the key.
   * @throws DataAccessException in case of data access exception.
   */
  public String getKey() throws DataAccessException {
    if (getMarcEncoding().getMarcTag().equals("700"))
      return "100" + "." + getCorrelation(2);
    else if (getMarcEncoding().getMarcTag().equals("710"))
      return "110" + "." + getCorrelation(2);
    else if (getMarcEncoding().getMarcTag().equals("711"))
      return "111" + "." + getCorrelation(2);
    else
      return getMarcEncoding().getMarcTag() + "." + getCorrelation(2);
  }

  //TODO: move in storageService and add session
  @Deprecated
  public List replaceEquivalentDescriptor(short indexingLanguage, int cataloguingView) throws DataAccessException {
		/* DAODescriptor dao = new DAONameDescriptor();
		List newTags = new ArrayList();
		Descriptor d = getDescriptor();
		REF ref = dao.getCrossReferencesWithLanguage(d, cataloguingView, indexingLanguage);
	    if (ref!=null) {
			AccessPoint aTag =	(AccessPoint) deepCopy(this);
			aTag.markNew();
			aTag.setDescriptor(dao.load(ref.getTarget(),cataloguingView));
			aTag.setHeadingNumber(new Integer(aTag.getDescriptor().getKey().getHeadingNumber()));
			newTags.add(aTag);
		}
	    return newTags; */

    return null;
  }

  //TODO move in storageService (as was: called in constructor) use configuration module to set function code
  public void setDefaultSourceCode() {
		/*
		short functionCode=0;
		functionCode= new Short(configHandler.findValue("t_nme_fnctn","nameAccessPoint.functionCode"));
		setFunctionCode(functionCode);
		*/
  }

  /**
   * Gets stringText for visualization mode with sub-field codes.
   *
   * @return stringText.
   */
  public StringText getStringText() {
    final StringText result = new StringText();
    result.add(getAccessPointStringText().getSubfieldsWithCodes("i"));
    if (getDescriptor() != null) {
      result.parse(getDescriptor().getStringText());
    }
    result.add(getAccessPointStringText().getSubfieldsWithoutCodes("i"));
    return result;
  }
}
