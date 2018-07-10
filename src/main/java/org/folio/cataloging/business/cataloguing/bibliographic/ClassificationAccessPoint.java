package org.folio.cataloging.business.cataloguing.bibliographic;

import org.folio.cataloging.business.cataloguing.common.OrderedTag;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.dao.persistence.Descriptor;
import org.folio.cataloging.dao.DAOBibliographicCorrelation;
import org.folio.cataloging.dao.persistence.CLSTN;
import org.folio.cataloging.dao.persistence.ClassificationType;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.shared.CorrelationValues;
import org.folio.cataloging.util.StringText;

import java.util.List;

/**
 * @author paulm
 * @version $Revision: 1.7 $, $Date: 2005/12/21 08:30:32 $
 * @since 1.0
 */

@SuppressWarnings("unchecked")
public class ClassificationAccessPoint extends BibliographicAccessPoint implements OrderedTag 
{
	private static final long serialVersionUID = 1L;
	
	private static final short CLASSIFICATION_TAG_CATEGORY = 6;
	private CLSTN descriptor = new CLSTN();
	private Integer sequenceNumber;

	public ClassificationAccessPoint() 
	{
		super();
		setFunctionCode(
		Defaults.getShort("classificationAccessPoint.functionCode"));
	}

	public ClassificationAccessPoint(int itemNbr) 
	{
		super(itemNbr);
		setFunctionCode(
		Defaults.getShort("classificationAccessPoint.functionCode"));
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.AccessPoint#getDescriptor()
	 */
	public Descriptor getDescriptor() {
		return descriptor;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.AccessPoint#setDescriptor(librisuite.hibernate.Descriptor)
	 */
	public void setDescriptor(Descriptor descriptor) {
		this.descriptor = (CLSTN) descriptor;
	}


	public void setDescriptorStringText(StringText stringText) {
		descriptor.setStringText(stringText.toString());
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.AccessPoint#getAccessPointStringText()
	 */
	public StringText getAccessPointStringText() {
		StringText stringText= new StringText();
		boolean isDewey = descriptor.getTypeCode()==12;
		Integer deweyEditionNumber = descriptor.getDeweyEditionNumber();
		if(isDewey && deweyEditionNumber!=null)
			stringText.addSubfield(new Subfield("2",""+deweyEditionNumber));
		 return stringText;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.AccessPoint#setAccesspointStringText(org.folio.cataloging.util.StringText)
	 */
	public void setAccessPointStringText(StringText stringText) {
		// nothing to do -- no apf text
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getCategory()
	 */
	public int getCategory() {
		return CLASSIFICATION_TAG_CATEGORY;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return getDescriptor().getCorrelationValues().change(
			2,
			getFunctionCode());
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getFirstCorrelationList()
	 */
	public List getFirstCorrelationList() throws DataAccessException {
		return getDaoCodeTable().getList(ClassificationType.class,true);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getSecondCorrelationList(short)
	 */
	public List getSecondCorrelationList(short value1) throws DataAccessException 
	{
		DAOBibliographicCorrelation dao = new DAOBibliographicCorrelation();
		/* Bug 4121 inizio */
		//return dao.getSecondCorrelationList(getCategory(),value1,ClassificationFunction.class);
		return dao.getClassificationTagLabels(getCategory(),value1);
		/* Bug 4121 fine */
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		setFunctionCode(v.getValue(2));
		getDescriptor().setCorrelationValues(v);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
	 */
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return (v.isValueDefined(2)) && (v.getValue(2) != getFunctionCode());
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#getRequiredEditPermission()
	 */
	public String getRequiredEditPermission() {
		return "editClassNumber";
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.Tag#getThirdCorrelationList(short, short)
	 */
	public List getThirdCorrelationList(short value1, short value2) throws DataAccessException {
		return null;
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof ClassificationAccessPoint))
			return false;
		ClassificationAccessPoint other = (ClassificationAccessPoint) obj;
		return super.equals(obj) && (other.functionCode == this.functionCode)&& (other.descriptor.getKey().getHeadingNumber() == this.descriptor.getKey().getHeadingNumber());
		// TODO don't know if this is right
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return super.hashCode(); // TODO this is bad, should be changed
	}
	
	/*public Integer getSequenceNumber() {
		return new Integer(sequenceNumber);
	}
	public void setSequenceNumber(Integer integer) {
		sequenceNumber = 0;
		if (integer != null) {
			sequenceNumber = integer.intValue();
		}
	}*/
	
	public void setSequenceNumber(Integer integer) {
		sequenceNumber = integer;
	}
	
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}
}