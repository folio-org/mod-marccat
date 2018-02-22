package org.folio.cataloging.business.cataloguing.bibliographic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.common.AccessPoint;
import org.folio.cataloging.business.cataloguing.common.OrderedTag;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.dao.DAOBibliographicCorrelation;
import org.folio.cataloging.dao.DAODescriptor;
import org.folio.cataloging.dao.DAOSubjectDescriptor;
import org.folio.cataloging.dao.persistence.*;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.util.StringText;

import java.util.ArrayList;
import java.util.List;

import static org.folio.cataloging.F.deepCopy;

public class SubjectAccessPoint extends BibliographicAccessPoint implements OrderedTag, Equivalent 
{
	private static final long serialVersionUID = -5339141299630141762L;
	private static final Log logger = LogFactory.getLog(SubjectAccessPoint.class);
	
	private static final String VARIANT_CODES = "34eu";
	private short functionCode = -1;
	private String workRelatorCode;
	private String workRelatorStringtext;
	private int sequenceNumber;
	private SBJCT_HDG descriptor = new SBJCT_HDG();

	public SubjectAccessPoint() 
	{
		super();
		setFunctionCode(Defaults.getShort("subjectAccessPoint.functionCode"));
	}

	public SubjectAccessPoint(int itemNbr) 
	{
		super(itemNbr);
		setFunctionCode(Defaults.getShort("subjectAccessPoint.functionCode"));
	}

	public short getFunctionCode() {
		return functionCode;
	}

	public Integer getSequenceNumber() {
		return new Integer(sequenceNumber);
	}

	public String getWorkRelatorCode() {
		return workRelatorCode;
	}

	public String getWorkRelatorStringtext() {
		return workRelatorStringtext;
	}

	public void setFunctionCode(short i) {
		functionCode = i;
	}

	public void setSequenceNumber(Integer integer) 
	{
		sequenceNumber = 0;
		if (integer != null) {
			sequenceNumber = integer.intValue();
		}
	}

	public void setWorkRelatorCode(String string) {
		workRelatorCode = string;
	}

	public void setWorkRelatorStringtext(String string) {
		workRelatorStringtext = string;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.bibliographic.Tag#setStringText(org.folio.cataloging.util.StringText)
	 */
	// public void setStringText(StringText stringText) {
	// TODO separate descriptor subfields from apf subfields
	// TODO flag the descriptor as "changed"?
	// descriptor.setStringText(stringText.toString());
	// }
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof SubjectAccessPoint))
			return false;
		SubjectAccessPoint other = (SubjectAccessPoint) obj;
		return super.equals(obj) && (other.functionCode == this.functionCode);
		// TODO don't know if this is right
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return super.hashCode(); // TODO this is bad, should be changed
	}

	public Descriptor getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(Descriptor sbjct_hdg) 
	{
		descriptor = (SBJCT_HDG) sbjct_hdg;
		try {
			getMarcEncoding();
		} catch (MarcCorrelationException e) {
			try {
				CorrelationValues v = getCorrelationValues();
				short v2 = new DAOBibliographicCorrelation()
				.getFirstAllowedValue2(getCategory(), v.getValue(1), v
						.getValue(3));
				setFunctionCode(v2);
			} catch (DataAccessException e1) {
				setFunctionCode((short)-1);
			}
		} catch (DataAccessException e) {
			// ignore
		} 
	}

	/*
	 * (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getFirstCorrelationList(java.util.Locale)
	 */
	public List getFirstCorrelationList() throws DataAccessException {
		return getDaoCodeTable().getList(SubjectType.class,true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getSecondCorrelationList(short,
	 *      java.util.Locale)
	 */
	public List getSecondCorrelationList(short value1)
			throws DataAccessException {
		DAOBibliographicCorrelation dao = new DAOBibliographicCorrelation();
		return dao.getSecondCorrelationList(getCategory(), value1,
				SubjectFunction.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getThirdCorrelationList(short,
	 *      short, java.util.Locale)
	 */
	public List getThirdCorrelationList(short value1, short value2)
			throws DataAccessException {
		DAOBibliographicCorrelation dao = new DAOBibliographicCorrelation();
		return dao.getThirdCorrelationList(getCategory(), value1, value2,
				SubjectSource.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.bibliographic.Tag#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
	 */
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return (v.isValueDefined(2)) && (v.getValue(2) != getFunctionCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getRequiredEditPermission()
	 */
	public String getRequiredEditPermission() {
		return "editSubject";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return getDescriptor().getCorrelationValues().change(2,
				getFunctionCode());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.bibliographic.Tag#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		setFunctionCode(v.getValue(2));
		getDescriptor().setCorrelationValues(v);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getCategory()
	 */
	public short getCategory() {
		return 4;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.bibliographic.AccessPoint#getAccessPointStringText()
	 */
	public StringText getAccessPointStringText() {
		StringText text = new StringText(workRelatorStringtext);
		text.parse(workRelatorCode);
		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.cataloguing.bibliographic.AccessPoint#setAccesspointStringText(org.folio.cataloging.util.StringText)
	 */
	public void setAccessPointStringText(StringText stringText) {
		// TODO _JANICK externalize codes
		workRelatorCode = stringText.getSubfieldsWithCodes("4").toString();
		workRelatorStringtext = stringText.getSubfieldsWithCodes("eu")
				.toString();
	}

	public void setDescriptorStringText(StringText stringText) {
		getDescriptor().setStringText(
				stringText.getSubfieldsWithoutCodes(VARIANT_CODES).toString());
	}
	
	public String getVariantCodes() {
		return VARIANT_CODES;
	}
	
	public List replaceEquivalentDescriptor(short indexingLanguage, int cataloguingView) throws DataAccessException 
	{
		DAODescriptor dao = new DAOSubjectDescriptor();
		List newTags = new ArrayList();
		Descriptor d = getDescriptor();
		REF ref = dao.getCrossReferencesWithLanguage(d, cataloguingView, indexingLanguage);
	    if (ref!=null) {
			//REF aRef = (REF)refs.get(0);
			/*Deve fare il replace del descrittore, non un nuovo tag altrimenti rimuovere il tag corrente*/
			AccessPoint aTag =	(AccessPoint) deepCopy(this);
			aTag.markNew();
			aTag.setDescriptor(dao.load(ref.getTarget(),cataloguingView));
			aTag.setHeadingNumber(new Integer(aTag.getDescriptor()
								 .getKey()
								 .getHeadingNumber()));
			 newTags.add(aTag);
		}
		return newTags;
	}
}