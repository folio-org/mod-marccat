/*
 * (c) LibriCore
 * 
 * Created on Dec 1, 2004
 * 
 * CLSTN.java
 */
package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.cataloguing.authority.AuthorityClassificationAccessPoint;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.descriptor.Descriptor;
import org.folio.cataloging.business.descriptor.SortFormParameters;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAOClassificationDescriptor;
import org.folio.cataloging.dao.DAOIndexList;
import org.folio.cataloging.shared.CorrelationValues;

/**
 * @author paulm
 * @version $Revision: 1.9 $, $Date: 2006/07/12 15:42:56 $
 * @since 1.0
 */
public class CLSTN extends Descriptor {
	private static final long serialVersionUID = 1L;
	private Integer deweyEditionNumber;
	private int typeCode;

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public CLSTN() {
		super();
		setTypeCode(Defaults.getShort("classification.typeCode"));
		setVerificationLevel(Defaults.getChar("classification.verificationLevel"));
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getReferenceClass()
	 */
	public Class getReferenceClass(Class targetClazz) {
		return null;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getDAO()
	 */
	public AbstractDAO getDAO() {
		return new DAOClassificationDescriptor();
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getAccessPointClass()
	 */
	public Class getAccessPointClass() {
		return ClassificationAccessPoint.class;
	}

	@Override
	public Class getAuthorityAccessPointClass() {
		return AuthorityClassificationAccessPoint.class;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getDefaultBrowseKey()
	 */
	public String getDefaultBrowseKey() {
		return "23P5";
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getNextNumberKeyFieldCode()
	 */
	public String getNextNumberKeyFieldCode() {
		return "LN";
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return new CorrelationValues(
			getTypeCode(),
			CorrelationValues.UNDEFINED,
			CorrelationValues.UNDEFINED);
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		setTypeCode(v.getValue(1));
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getSortFormParameters()
	 */
	public SortFormParameters getSortFormParameters() {
		return new SortFormParameters(400, getTypeCode(), 0, 0, 0);
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getCategory()
	 */
	public int getCategory() {
		return 20;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public Integer getDeweyEditionNumber() {
		return deweyEditionNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getTypeCode() {
		return typeCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setDeweyEditionNumber(Integer short1) {
		deweyEditionNumber = short1;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setTypeCode(int s) {
		typeCode = s;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getBrowseKey()
	 */
	public String getBrowseKey() {
		String result = null;
		DAOIndexList dao = new DAOIndexList();
		try {
			result = dao.getIndexBySortFormType(400, getTypeCode());
			if (result != null) {
				return result;
			}
			else {
				return super.getBrowseKey();
			}
		} catch (DataAccessException e) {
			return super.getBrowseKey();
		}
	}

	/* (non-Javadoc)
	 * @see Descriptor#getHeadingNumberSearchIndexKey()
	 */
	public String getHeadingNumberSearchIndexKey() {
		return "233P";
	}
	public String getLockingEntityType() {
		return "LN";
	}

}
