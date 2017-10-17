/*
 * (c) LibriCore
 * 
 * Created on 21-jun-2004
 * 
 * SHLF_LIST.java
 */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;

import librisuite.business.cataloguing.mades.MAD_SHLF_LIST_ACS_PNT;
import librisuite.business.common.CorrelationValues;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Defaults;
import librisuite.business.descriptor.DAOMadesShelfList;
import librisuite.business.descriptor.Descriptor;
import librisuite.business.descriptor.SortFormParameters;
import librisuite.business.searching.DAOIndexList;

import com.libricore.librisuite.common.HibernateUtil;

public class MAD_SHLF_LIST extends Descriptor implements Serializable {

	private int shelfListKeyNumber;
	private int mainLibraryNumber;
	private char typeCode = Defaults.getChar("mades.shelflist.type");

	/**
	 * @return mainLibraryNumber
	 */
	public int getMainLibraryNumber() {
		return mainLibraryNumber;
	}

	/**
	 * Getter for shelfListKeyNumber
	 * 
	 * @return shelfListKeyNumber
	 */
	public int getShelfListKeyNumber() {
		return shelfListKeyNumber;
	}

	/**
	 * Getter for shelfListTypeCode
	 * 
	 * @return shelfListTypeCode
	 */
	public char getTypeCode() {
		return typeCode; 
	}

	/**
	 * Setter for mainLibraryNumber
	 * 
	 * @param i mainLibraryNumber
	 */
	public void setMainLibraryNumber(int i) {
		mainLibraryNumber = i;
	}

	/**
	 * Setter for shelfListKeyNumber
	 * 
	 * @param i shelfListKeyNumber
	 */
	public void setShelfListKeyNumber(int i) {
		shelfListKeyNumber = i;
		getKey().setHeadingNumber(i);   // to preserve Descriptor behaviour
	}
	/**
	 * Setter for shelfListTypeCode
	 * 
	 * @param c shelfListTypeCode
	 */
	public void setTypeCode(char c) {
		typeCode = c;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getAccessPointClass()
	 */
	public Class getAccessPointClass() {
		return MAD_SHLF_LIST_ACS_PNT.class;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getCategory()
	 */
	public short getCategory() {
		// TODO 14 is 852 -- somewhat misleading
		return 14;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return new CorrelationValues(
			(short)1,// TODO _MIKE: verificare con Paul perchè la @ non viene interpretata
			CorrelationValues.UNDEFINED,
			CorrelationValues.UNDEFINED);
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getDAO()
	 */
	public HibernateUtil getDAO() {
		return new DAOMadesShelfList();
	}
	
	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getBrowseKey()
	 */
	public String getBrowseKey() {
		String result = null;
		DAOIndexList dao = new DAOIndexList();
		try {
			result = dao.getIndexBySortFormType((short)200, (short)getTypeCode());
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
	 * @see librisuite.hibernate.Descriptor#getDefaultBrowseKey()
	 */
	public String getDefaultBrowseKey() {
		return "284P30";
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getNextNumberKeyFieldCode()
	 */
//	public String getNextNumberKeyFieldCode() { 
//		return mades.MadesConstants.MNNK_MAD_SHELF_KEY_NBR;
//	}
	public String getNextNumberKeyFieldCode() { 
		return null;
	}
	
	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getReferenceClass()
	 */
	public Class getReferenceClass(Class targetClazz) {
		return null;
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#setCorrelationValues(librisuite.business.common.CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		setTypeCode((char)v.getValue(1));
	}
	
	/* (non-Javadoc)
	 * @see librisuite.business.common.Persistence#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		/*
		 * The default implementation sets key.headingNumber to the new value.
		 * MAD_SHLF_LIST differs from most descriptors in that the key field is not
		 * used (since there are no user views)
		 */
		super.generateNewKey();
		setShelfListKeyNumber(getKey().getHeadingNumber());
	}

	/* (non-Javadoc)
	 * @see librisuite.hibernate.Descriptor#getSortFormParameters()
	 */
	public SortFormParameters getSortFormParameters() {
		return new SortFormParameters(200, getTypeCode(), 0, 0, 0);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.Descriptor#isCanTransfer()
	 */
	public boolean isCanTransfer() {
		return false;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.Descriptor#getHeadingNumberSearchIndexKey()
	 */
	public String getHeadingNumberSearchIndexKey() {
		return "232P";
	}
	public String getLockingEntityType() {
		return "FL";
	}

}
