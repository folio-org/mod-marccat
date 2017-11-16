/*
 * (c) LibriCore
 * 
 * Created on Dec 1, 2004
 * 
 * CLSTN.java
 */
package librisuite.hibernate;

import librisuite.business.cataloguing.authority.AuthorityClassificationAccessPoint;
import librisuite.business.cataloguing.bibliographic.ClassificationAccessPoint;
import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Defaults;
import librisuite.business.descriptor.DAOClassificationDescriptor;
import librisuite.business.descriptor.Descriptor;
import librisuite.business.descriptor.SortFormParameters;
import librisuite.business.searching.DAOIndexList;

import com.libricore.librisuite.common.HibernateUtil;

/**
 * @author paulm
 * @version $Revision: 1.9 $, $Date: 2006/07/12 15:42:56 $
 * @since 1.0
 */
public class CLSTN extends Descriptor {
	private static final long serialVersionUID = 1L;
	private Short deweyEditionNumber;
	private short typeCode;

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
	public HibernateUtil getDAO() {
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
	public short getCategory() {
		return 20;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public Short getDeweyEditionNumber() {
		return deweyEditionNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public short getTypeCode() {
		return typeCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setDeweyEditionNumber(Short short1) {
		deweyEditionNumber = short1;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setTypeCode(short s) {
		typeCode = s;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getBrowseKey()
	 */
	public String getBrowseKey() {
		String result = null;
		DAOIndexList dao = new DAOIndexList();
		try {
			result = dao.getIndexBySortFormType((short)400, getTypeCode());
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
	 * @see librisuite.business.descriptor.Descriptor#getHeadingNumberSearchIndexKey()
	 */
	public String getHeadingNumberSearchIndexKey() {
		return "233P";
	}
	public String getLockingEntityType() {
		return "LN";
	}

}
