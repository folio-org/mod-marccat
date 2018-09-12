package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.cataloguing.authority.AuthorityControlNumberAccessPoint;
import org.folio.cataloging.business.cataloguing.bibliographic.ControlNumberAccessPoint;
import org.folio.cataloging.business.descriptor.SortFormParameters;
import org.folio.cataloging.dao.ControlNumberDescriptorDAO;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.shared.CorrelationValues;

/**
 * Hibernate class for table CNTL_NBR.
 *
 * @author paulm
 * @author carment
 */
public class CNTL_NBR extends Descriptor {

	/** The type code. */
	private int typeCode;

	/**
	 * Instantiates a new cntl nbr.
	 */
	public CNTL_NBR() {
		super();
	}

	/* (non-Javadoc)
	 * @see Descriptor#getReferenceClass(java.lang.Class)
	 */
	public Class getReferenceClass(Class targetClazz) {
		return null;
	}

	/**
	 * Gets the dao.
	 *
	 * @return the dao
	 */
	public HibernateUtil getDAO() {
		return new ControlNumberDescriptorDAO();
	}

	/* (non-Javadoc)
	 * @see Descriptor#getAccessPointClass()
	 */
	public Class getAccessPointClass() {
		return ControlNumberAccessPoint.class;
	}

	/* (non-Javadoc)
	 * @see Descriptor#getAuthorityAccessPointClass()
	 */
	@Override
	public Class getAuthorityAccessPointClass() {
		return AuthorityControlNumberAccessPoint.class;
	}

	/* (non-Javadoc)
	 * @see Descriptor#getDefaultBrowseKey()
	 */
	public String getDefaultBrowseKey() {
		return "16P30";
	}

	/* (non-Javadoc)
	 * @see Descriptor#getNextNumberKeyFieldCode()
	 */
	public String getNextNumberKeyFieldCode() {
		return "RN";
	}

	/* (non-Javadoc)
	 * @see Descriptor#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return new CorrelationValues(
				getTypeCode(),
				CorrelationValues.UNDEFINED,
				CorrelationValues.UNDEFINED);
	}


	/* (non-Javadoc)
	 * @see Descriptor#setCorrelationValues(CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		setTypeCode(v.getValue(1));
	}

	/* (non-Javadoc)
	 * @see Descriptor#getSortFormParameters()
	 */
	public SortFormParameters getSortFormParameters() {
		return new SortFormParameters(300, getTypeCode(), 0, 0, 0);
	}

	/* (non-Javadoc)
	 * @see Descriptor#getCategory()
	 */
	public int getCategory() {
		return 19;
	}

	/**
	 * Gets the type code.
	 *
	 * @return the type code
	 */
	public int getTypeCode() {
		return typeCode;
	}

	/**
	 * Sets the type code.
	 *
	 * @param s the new type code
	 */
	public void setTypeCode(int s) {
		typeCode = s;
	}

	/* (non-Javadoc)
	 * @see Descriptor#getHeadingNumberSearchIndexKey()
	 */
	public String getHeadingNumberSearchIndexKey() {
		return "231P";
	}

	/* (non-Javadoc)
	 * @see Descriptor#getLockingEntityType()
	 */
	public String getLockingEntityType() {
		return "RN";
	}

}
