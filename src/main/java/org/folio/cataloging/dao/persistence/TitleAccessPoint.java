package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.cataloguing.bibliographic.Equivalent;
import org.folio.cataloging.business.cataloguing.bibliographic.NameTitleComponent;
import org.folio.cataloging.business.cataloguing.common.OrderedTag;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.integration.GlobalStorage;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.util.StringText;

import java.util.List;

/**
 * Persistent class for  TTL_ACS_PNT.
 *
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */

//TODO: many doubts about presence here of correlation and stringText

@SuppressWarnings("unchecked")
public class TitleAccessPoint extends NameTitleComponent implements OrderedTag, Equivalent {
  private static final long serialVersionUID = 1636144329543139231L;

  private String institution;
  private Integer seriesIssnHeadingNumber;
  private int secondaryFunctionCode;
  private String volumeNumberDescription;
  private String variantTitle;
  private TTL_HDG descriptor = new TTL_HDG ( );
  private Integer sequenceNumber;
  private String issnText;

  public TitleAccessPoint() {
  }

  public TitleAccessPoint(final int itemNbr) {
    super (itemNbr);
  }

  /**
   * Gets institution from title access point.
   *
   * @return institution.
   */
  public String getInstitution() {
    return institution;
  }

  /**
   * Sets institution param to title access point.
   *
   * @param institution -- the institution param to set.
   */
  public void setInstitution(final String institution) {
    this.institution = institution;
  }

  /**
   * Compares an object with another one.
   *
   * @param obj -- the object to compare.
   * @return true if equals.
   */
  public boolean equals(final Object obj) {
    if (!(obj instanceof TitleAccessPoint))
      return false;
    final TitleAccessPoint other = (TitleAccessPoint) obj;
    return super.equals (obj)
      && (other.getFunctionCode ( ) == this.getFunctionCode ( ))
      && (other.nameTitleHeadingNumber == this.nameTitleHeadingNumber);
  }

  @Override
  public int hashCode() {
    return super.hashCode ( );
  }

  /**
   * Gets descriptor associated to title access point.
   *
   * @return descriptor.
   */
  public Descriptor getDescriptor() {
    return descriptor;
  }

  /**
   * Sets descriptor.
   *
   * @param ttl_hdg -- the descriptor associated to title access point.
   */
  public void setDescriptor(final Descriptor ttl_hdg) {
    descriptor = (TTL_HDG) ttl_hdg;
  }

  /**
   * Gets secondary function code associated to title access point.
   *
   * @return secondaryFunctionCode.
   */
  public int getSecondaryFunctionCode() {
    return secondaryFunctionCode;
  }

  /**
   * Sets secondary function code to title access point.
   *
   * @param s -- secondary function code associated.
   */
  public void setSecondaryFunctionCode(final int s) {
    secondaryFunctionCode = s;
  }

  /**
   * Gets series issn heading number associated to title access point.
   *
   * @return seriesIssnHeadingNumber.
   */
  public Integer getSeriesIssnHeadingNumber() {
    return seriesIssnHeadingNumber;
  }

  /**
   * Sets issn heading number to title access point.
   *
   * @param issnHeadingNumber -- the issn number to set.
   */
  public void setSeriesIssnHeadingNumber(final Integer issnHeadingNumber) {
    seriesIssnHeadingNumber = issnHeadingNumber;
  }

  /**
   * Gets variant title associated to title access point.
   *
   * @return variantTitle.
   */
  public String getVariantTitle() {
    return variantTitle;
  }

  /**
   * Sets variant title to title access point.
   *
   * @param variantTitle -- the variant title to set.
   */
  public void setVariantTitle(final String variantTitle) {
    this.variantTitle = variantTitle;
  }

  /**
   * Gets volume number description associated to title access point.
   *
   * @return volumeNumberDescription.
   */
  public String getVolumeNumberDescription() {
    return volumeNumberDescription;
  }

  /**
   * Sets volume number description to title access point.
   *
   * @param volumeNumberDescription -- the volume number description to set.
   */
  public void setVolumeNumberDescription(final String volumeNumberDescription) {
    this.volumeNumberDescription = volumeNumberDescription;
  }

  /**
   * Gets permission string to compare with authorization agent.
   *
   * @return "editTitle".
   */
  public String getRequiredEditPermission() {
    return GlobalStorage.TITLE_REQUIRED_PERMISSION;
  }

  /**
   * Checks correlation key value is changed for title access point.
   *
   * @param v -- the correlation values.
   * @return boolean.
   */
  public boolean correlationChangeAffectsKey(final CorrelationValues v) {
    return (v.isValueDefined (1) && (v.getValue (1) != getFunctionCode ( )));
  }

  /**
   * Gets correlation values of title descriptor.
   *
   * @return correlationValues.
   */
  public CorrelationValues getCorrelationValues() {
    return getDescriptor ( ).getCorrelationValues ( ).change (1, getFunctionCode ( )).change (2, getSecondaryFunctionCode ( ));
  }

  /**
   * Sets correlation values of title descriptor.
   *
   * @param v -- the correlation values to set.
   */
  public void setCorrelationValues(final CorrelationValues v) {
    setFunctionCode (v.getValue (1));
    setSecondaryFunctionCode (v.getValue (2));
    getDescriptor ( ).setCorrelationValues (v);
  }

  /**
   * Gets stringText for visualization mode with sub-field codes.
   *
   * @return stringText.
   */
  public StringText getStringText() {
    final StringText result = new StringText ( );
    result.add (getAccessPointStringText ( ).getSubfieldsWithCodes ("i"));
    if (getDescriptor ( ) != null) {
      result.parse (getDescriptor ( ).getStringText ( ));
    }
    result.add (getAccessPointStringText ( ).getSubfieldsWithoutCodes ("i"));
    return result;
  }

  /**
   * Gets the stringText associated to title access point.
   *
   * @return stringText.
   */
  public StringText getAccessPointStringText() {
    StringText text = new StringText (variantTitle);
    text.parse (institution);
    if (this.getSeriesIssnHeadingNumber ( ) != null)
      text.addSubfield (new Subfield (GlobalStorage.TITLE_ISSN_SERIES_SUBFIELD_CODE, "" + getISSNText ( )));
    text.parse (volumeNumberDescription);
    return text;
  }

  /**
   * Sets stringText to title access point.
   *
   * @param stringText -- the stringText to set.
   */
  public void setAccessPointStringText(final StringText stringText) {
    variantTitle = stringText.getSubfieldsWithCodes ("ci").toString ( );
    institution = stringText.getSubfieldsWithCodes (GlobalStorage.NAME_TITLE_INSTITUTION_SUBFIELD_CODE).toString ( );
    if (!stringText.getSubfieldsWithCodes (GlobalStorage.TITLE_ISSN_SERIES_SUBFIELD_CODE).isEmpty ( ) && this.getSeriesIssnHeadingNumber ( ) != null) {
      seriesIssnHeadingNumber = this.getSeriesIssnHeadingNumber ( );
    } else {
      seriesIssnHeadingNumber = null;
    }
    volumeNumberDescription = stringText.getSubfieldsWithCodes (GlobalStorage.TITLE_VOLUME_SUBFIELD_CODE).toString ( );
  }

  /**
   * Sets descriptor string text.
   *
   * @param stringText -- the string text to set.
   */
  public void setDescriptorStringText(final StringText stringText) {
    getDescriptor ( ).setStringText (
      stringText.getSubfieldsWithoutCodes (getVariantCodes ( )).toString ( ));
  }

  /**
   * Gets title marc category code.
   *
   * @return category.
   */
  public int getCategory() {
    return GlobalStorage.TITLE_CATEGORY;
  }


  /**
   * Gets variant sub-field codes.
   *
   * @return variant codes.
   */
  public String getVariantCodes() {
    return GlobalStorage.TITLE_VARIANT_CODES;
  }

  /**
   * Gets issn text associated to series issn heading number.
   *
   * @return issnText.
   */
  public String getISSNText() {
    return issnText;
  }

  /**
   * Sets issn text associated to series issn heading number.
   *
   * @param issnText -- the issn text to set.
   */
  public void setIssnText(final String issnText) {
    this.issnText = issnText;
  }

  /**
   * Gets the sequence number associated to title access point.
   *
   * @return sequenceNumber.
   */
  public Integer getSequenceNumber() {
    return sequenceNumber;
  }

  /**
   * Sets sequence number to title access point.
   *
   * @param sequenceNbr -- the sequence number to set.
   */
  public void setSequenceNumber(final Integer sequenceNbr) {
    this.sequenceNumber = sequenceNbr;
    super.setSequenceNumber (sequenceNumber);
  }

  //TODO: move in storageService and add session
  public List replaceEquivalentDescriptor(final short indexingLanguage, int cataloguingView) throws DataAccessException {
		/* DAODescriptor dao = new DAOTitleDescriptor();
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

  //TODO move in storageService and set after creation (as was: called in constructor)
  public void setDefaultTypeAndFunction() {
		/*int typCode= new Integer(configHandler.findValue("t_ttl_fnctn_2nd_fnctn","titleAccessPoint.functionCode"));
		int type = configHandler.isParamOfGlobalVariable("t_ttl_fnctn_2nd_fnctn") ? this.getType(typCode) : typCode;

		setFunctionCode((short)type);
		int funCode= new Integer(configHandler.findValue("t_ttl_fnctn_2nd_fnctn","titleAccessPoint.secondaryFunctionCode"));
		int function= this.getFunction(funCode);
		setSecondaryFunctionCode((short)function);*/
  }
}
