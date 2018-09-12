package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.cataloguing.bibliographic.BibliographicAccessPoint;
import org.folio.cataloging.business.cataloguing.bibliographic.Equivalent;
import org.folio.cataloging.business.cataloguing.common.OrderedTag;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.integration.GlobalStorage;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.util.StringText;

import java.util.List;

/**
 * Persistent class for SBJCT_ACS_PNT.
 *
 * @author nbianchini
 */
public class SubjectAccessPoint extends BibliographicAccessPoint implements OrderedTag, Equivalent
{
	private static final long serialVersionUID = -5339141299630141762L;

	private int functionCode = -1;
	private String workRelatorCode;
	private String workRelatorStringtext;
	private int sequenceNumber;
	private SBJCT_HDG descriptor = new SBJCT_HDG();

	public SubjectAccessPoint()
	{
		super();
	}

	public SubjectAccessPoint(final int itemNbr)
	{
		super(itemNbr);
	}

    /**
     *
     * @return the subject access point function code.
     */
	public int getFunctionCode() {
		return functionCode;
	}

    /**
     * Gets the sequence number associated to access point.
     *
     * @return sequenceNumber.
     */
	public Integer getSequenceNumber() {
		return new Integer(sequenceNumber);
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
     * Gets name relator string text if is a name subject.
     *
     * @return the work relator string text.
     */
	public String getWorkRelatorStringtext() {
		return workRelatorStringtext;
	}

    /**
     * Sets function code associated to subject access point.
     *
     * @param code -- the function code to set.
     */
	public void setFunctionCode(final int code) {
		functionCode = code;
	}

    /**
     * Sets sequence number to access point.
     *
     * @param sequenceNbr -- the sequence number to set.
     */
    public void setSequenceNumber(final Integer sequenceNbr)
	{
		sequenceNumber = (sequenceNbr != null) ?sequenceNbr :0;
		super.setSequenceNumber(sequenceNumber);
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
	public boolean equals(final Object obj) {
		if (!(obj instanceof SubjectAccessPoint))
			return false;
		final SubjectAccessPoint other = (SubjectAccessPoint) obj;
		return super.equals(obj) && (other.functionCode == this.functionCode);
	}

    /**
     *
     * @return hashCode.
     */
	public int hashCode() {
		return super.hashCode(); // TODO this is bad, should be changed
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
	public void setDescriptor(final Descriptor sbjct_hdg)
	{
		descriptor = (SBJCT_HDG) sbjct_hdg;
	}

    /**
     * Checks correlation key value is changed for subject access point.
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
     * @return "editSubject".
     */
	public String getRequiredEditPermission() {
		return GlobalStorage.SUBJECT_REQUIRED_PERMISSION ;
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
	public int getCategory() {
		return GlobalStorage.SUBJECT_CATEGORY;
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
		workRelatorCode = stringText.getSubfieldsWithCodes(GlobalStorage.WORK_REL_SUBFIELD_CODE).toString();
		workRelatorStringtext = stringText.getSubfieldsWithCodes(GlobalStorage.SUBJECT_WORK_REL_STRING_TEXT_SUBFIELD_CODES).toString();
	}

    /**
     * Sets descriptor string text.
     *
     * @param stringText -- the string text to set.
     */
	public void setDescriptorStringText(final StringText stringText) {
		getDescriptor().setStringText(stringText.getSubfieldsWithoutCodes(GlobalStorage.SUBJECT_VARIANT_CODES).toString());
	}

    /**
     *
     * @return variant subfield codes.
     */
	public String getVariantCodes() {
		return GlobalStorage.SUBJECT_VARIANT_CODES;
	}

    //TODO: move in storageService and add session
    @Deprecated
	public List replaceEquivalentDescriptor(short indexingLanguage, int cataloguingView) throws DataAccessException
	{
		/*DAODescriptor dao = new DAOSubjectDescriptor();
		List newTags = new ArrayList();
		Descriptor d = getDescriptor();
		REF ref = dao.getCrossReferencesWithLanguage(d, cataloguingView, indexingLanguage);
	    if (ref!=null) {
			//REF aRef = (REF)refs.get(0);
			//Deve fare il replace del descrittore, non un nuovo tag altrimenti rimuovere il tag corrente
			AccessPoint aTag =	(AccessPoint) deepCopy(this);
			aTag.markNew();
			aTag.setDescriptor(dao.load(ref.getTarget(),cataloguingView));
			aTag.setHeadingNumber(new Integer(aTag.getDescriptor()
								 .getKey()
								 .getHeadingNumber()));
			 newTags.add(aTag);
		}
		return newTags;
		*/
		return null;
	}
}
