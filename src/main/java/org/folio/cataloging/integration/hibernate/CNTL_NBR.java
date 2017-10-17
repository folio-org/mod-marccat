/*
 * (c) LibriCore
 * 
 * Created on Dec 1, 2004
 * 
 * CNTL_NBR.java
 */
package org.folio.cataloging.integration.hibernate;

import librisuite.business.cataloguing.authority.AuthorityControlNumberAccessPoint;
import librisuite.business.cataloguing.bibliographic.ControlNumberAccessPoint;
import librisuite.business.common.ConfigHandler;
import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Defaults;
import librisuite.business.descriptor.DAOControlNumberDescriptor;
import librisuite.business.descriptor.Descriptor;
import librisuite.business.descriptor.SortFormParameters;
import librisuite.business.searching.DAOIndexList;

import com.libricore.librisuite.common.HibernateUtil;

/**
 * @author paulm
 * @version $Revision: 1.8 $, $Date: 2007/03/05 12:36:04 $
 * @since 1.0
 */
public class CNTL_NBR extends Descriptor {
	private static final long serialVersionUID = 1L;
	private short typeCode;
	private ConfigHandler configHandler =ConfigHandler.getInstance();
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public CNTL_NBR() {
		super();
		//setTypeCode(Defaults.getShort("controlNumber.typeCode"));
		setDefaultTypeCode();
		setVerificationLevel(Defaults.getChar("controlNumber.verificationLevel"));
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
		return new DAOControlNumberDescriptor();
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getAccessPointClass()
	 */
	public Class getAccessPointClass() {
		return ControlNumberAccessPoint.class;
	}

	@Override
	public Class getAuthorityAccessPointClass() {
		return AuthorityControlNumberAccessPoint.class;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getDefaultBrowseKey()
	 */
	public String getDefaultBrowseKey() {
		try {
			if(getTypeCode()==20||getTypeCode()==21||getTypeCode()==22)
			   return "34P20";
			else if(getTypeCode()==67||getTypeCode()==68)
			  return "35P20";
			else if(getTypeCode()==37||getTypeCode()==40||getTypeCode()==43||getTypeCode()==46||getTypeCode()==49)
			  return "30P4";
			//new Canadian
			else if(getTypeCode()==93)
				 return "20P3";
			else if(getTypeCode()==3)
				 return "16P30";
			else
			   return Defaults.getString("control.number.browse.index");
			
		} catch (RuntimeException e) {
			return "16P30";
		}
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getNextNumberKeyFieldCode()
	 */
	public String getNextNumberKeyFieldCode() {
		return "RN";
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
		return new SortFormParameters(300, getTypeCode(), 0, 0, 0);
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getCategory()
	 */
	public short getCategory() {
		return 19;
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
			//TODO this should use getSortFormParameters			
			result = dao.getIndexBySortFormType((short) 300, getTypeCode());
			if (result != null) {
				return result;
			} else {
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
		return "231P";
	}
	public String getLockingEntityType() {
		return "RN";
	}
	public void setDefaultTypeCode(){		
		int typCode= new Integer(configHandler.findValue("t_cntl_nbr_typ_fnctn","controlNumber.typeCode"));
		int type = configHandler.isParamOfGlobalVariable("t_cntl_nbr_typ_fnctn") ? this.getType(typCode) : typCode;
		setTypeCode((short)type);
	}
}
