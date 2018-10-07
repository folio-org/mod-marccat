package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.cataloguing.bibliographic.Equivalent;
import org.folio.cataloging.business.cataloguing.bibliographic.NameTitleComponent;
import org.folio.cataloging.business.cataloguing.common.OrderedTag;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAONameTitleAccessPoint;
import org.folio.cataloging.integration.GlobalStorage;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.util.StringText;

import java.util.List;

/**
 * Persistent class to manage name-title heading.
 *
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class NameTitleAccessPoint extends NameTitleComponent implements OrderedTag, Equivalent {

  private static final DAONameTitleAccessPoint daoNameTitleAccessPoint = new DAONameTitleAccessPoint ( );
  private NME_TTL_HDG descriptor = new NME_TTL_HDG ( );
  private String institution;
  private int secondaryFunctionCode;
  private String seriesIssnHeadingNumber;
  private String volumeNumberDescription;
  private Integer sequenceNumber;

  public NameTitleAccessPoint() {
    super ( );
    //setDefaultFunctionCode();
    //setSecondaryFunctionCode(new Short(Defaults.getShort("nameTitleAccessPoint.secondaryFunctionCode")));
  }

  //TODO: set functionCode/secondaryFunctionCode using configuration module
  public NameTitleAccessPoint(final int functionCode, final int secondaryFunctionCode) {
    super ( );
    setFunctionCode (functionCode);
    this.secondaryFunctionCode = secondaryFunctionCode;
  }

  public NameTitleAccessPoint(final int itemNbr) {
    super (itemNbr);
  }

  /**
   * Gets the stringText associated to name-title access point.
   *
   * @return stringText.
   */
  public StringText getAccessPointStringText() {
    final StringText text = new StringText (volumeNumberDescription);
    text.parse (institution);
    return text;
  }

  /**
   * Sets stringText to name-title access point.
   *
   * @param stringText -- the stringText to set.
   */
  public void setAccessPointStringText(final StringText stringText) {
    volumeNumberDescription = stringText.getSubfieldsWithCodes (GlobalStorage.TITLE_VOLUME_SUBFIELD_CODE).toString ( );
    institution = stringText.getSubfieldsWithCodes (GlobalStorage.NAME_TITLE_INSTITUTION_SUBFIELD_CODE).toString ( );
  }

  /**
   * Gets correlation values of name-title descriptor.
   *
   * @return correlationValues.
   */
  public CorrelationValues getCorrelationValues() {
    CorrelationValues v = getDescriptor ( ).getCorrelationValues ( );
    v = v.change (3, v.getValue (2));
    v = v.change (2, v.getValue (1));
    v = v.change (1, getFunctionCode ( ));
    return v;
  }

  /**
   * Sets correlation values of name-title descriptor.
   *
   * @param v -- the correlation values to set.
   */
  public void setCorrelationValues(final CorrelationValues v) {
    setFunctionCode (v.getValue (1));
    CorrelationValues v2 = new CorrelationValues (v.getValue (2), v.getValue (3), CorrelationValues.UNDEFINED);
		/*v = v.change(1, v.getValue(2));
		v = v.change(2, v.getValue(3));
		v = v.change(3, CorrelationValues.UNDEFINED);*/
    getDescriptor ( ).setCorrelationValues (v2);
  }

  /**
   * Gets descriptor associated to name-title access point.
   *
   * @return descriptor.
   */
  public Descriptor getDescriptor() {
    return descriptor;
  }

  /**
   * Sets descriptor.
   *
   * @param descriptor -- the descriptor associated to title access point.
   */
  public void setDescriptor(final Descriptor descriptor) {
    this.descriptor = (NME_TTL_HDG) descriptor;
  }

  /**
   * Gets institution associated to name-title access point.
   *
   * @return institution.
   */
  public String getInstitution() {
    return institution;
  }

  /**
   * Sets institution param to name-title access point.
   *
   * @param institution -- the institution param to set.
   */
  public void setInstitution(final String institution) {
    this.institution = institution;
  }

  /**
   * Gets the secondary function code related to title.
   *
   * @return secondaryFunctionCode.
   */
  public int getSecondaryFunctionCode() {
    return secondaryFunctionCode;
  }

  /**
   * Sets secondary function code to name-title access point.
   *
   * @param s -- secondary function code associated.
   */
  public void setSecondaryFunctionCode(final int s) {
    secondaryFunctionCode = s;
  }

  /**
   * Gets series issn heading number associated to name-title access point.
   *
   * @return seriesIssnHeadingNumber.
   */
  public String getSeriesIssnHeadingNumber() {
    return seriesIssnHeadingNumber;
  }

  /**
   * Sets issn heading number to title access point.
   *
   * @param issnHeadingNumber -- the issn number to set.
   */
  public void setSeriesIssnHeadingNumber(final String issnHeadingNumber) {
    seriesIssnHeadingNumber = issnHeadingNumber;
  }

  /**
   * Gets volume number description associated to name-title access point.
   *
   * @return volumeNumberDescription.
   */
  public String getVolumeNumberDescription() {
    return volumeNumberDescription;
  }

  /**
   * Gets volume number description associated to title access point.
   *
   * @return volumeNumberDescription.
   */
  public void setVolumeNumberDescription(final String volume) {
    volumeNumberDescription = volume;
  }

  /**
   * Sets descriptor stringText.
   *
   * @param stringText -- the stringText to set.
   */
  public void setDescriptorStringText(StringText stringText) {
    getDescriptor ( ).setStringText (stringText.getSubfieldsWithoutCodes (GlobalStorage.NAME_TITLE_VARIANT_CODES).toString ( ));
  }

  /**
   * @return the associated dao.
   */
  public AbstractDAO getDAO() {
    return daoNameTitleAccessPoint;
  }

  public String getVariantCodes() {
    return GlobalStorage.NAME_TITLE_VARIANT_CODES;
  }

  /**
   * Gets the sequence number associated to name-title access point.
   *
   * @return sequenceNumber.
   */
  public Integer getSequenceNumber() {
    return sequenceNumber;
  }

  /**
   * Sets sequence number to title access point.
   *
   * @param sequenceNumber -- the sequence number to set.
   */
  public void setSequenceNumber(final Integer sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
    super.setSequenceNumber (sequenceNumber);
  }

  //TODO: move in storageService and add session
  @Deprecated
  public List replaceEquivalentDescriptor(short indexingLanguage,
                                          int cataloguingView) throws DataAccessException {
		/*DAODescriptor dao = new DAONameTitleDescriptor();
		List newTags = new ArrayList();
		Descriptor d = getDescriptor();
		REF ref = dao.getCrossReferencesWithLanguage(d, cataloguingView,
				indexingLanguage);
		if (ref!=null) {
			AccessPoint aTag = (AccessPoint) deepCopy(this);
			aTag.markNew();
			aTag.setDescriptor(dao.load(ref.getTarget(), cataloguingView));
			aTag.setHeadingNumber(new Integer(aTag.getDescriptor().getKey()
					.getHeadingNumber()));
			newTags.add(aTag);
		}
		return newTags;*/
    return null;
  }

  //TODO move in storageService (as was: called in constructor) use configuration module to set function code
  public void setDefaultFunctionCode() {
		/*short functionCode=0;
		functionCode= new Short(configHandler.findValue("t_nme_ttl_fnctn","nameTitleAccessPoint.functionCode"));
		setFunctionCode(functionCode);*/

  }


}
