package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.descriptor.SortFormParameters;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.ShelfListDAO;
import org.folio.cataloging.shared.CorrelationValues;

import java.io.Serializable;

/**
 * * Hibernate class for table SHLF_LIST.
 *
 * @author paulm
 * @author carment
 */
public class SHLF_LIST extends Descriptor implements Serializable {

	/** The shelf list key number. */
	private int shelfListKeyNumber;

	/** The main library number. */
	private int mainLibraryNumber;

	/** The type code. */
	private char typeCode;

	/** The amicus number. */
	private Integer amicusNumber;

	/**
	 * Instantiates a new shlf list.
	 */
	public SHLF_LIST() {
		super();
	}

	/**
	 * Gets the amicus number.
	 *
	 * @return the amicus number
	 */
	public Integer getAmicusNumber() {
		return amicusNumber;
	}

	/**
	 * Sets the amicus number.
	 *
	 * @param amicusNumber the new amicus number
	 */
	public void setAmicusNumber(Integer amicusNumber) {
		this.amicusNumber = amicusNumber;
	}

	/**
	 * Gets the main library number.
	 *
	 * @return the main library number
	 */
	public int getMainLibraryNumber() {
		return mainLibraryNumber;
	}

	/**
	 * Gets the shelf list key number.
	 *
	 * @return the shelf list key number
	 */
	public int getShelfListKeyNumber() {
		return shelfListKeyNumber;
	}

	/**
	 * Gets the type code.
	 *
	 * @return the type code
	 */
	public char getTypeCode() {
		return typeCode;
	}

	/**
	 * Sets the main library number.
	 *
	 * @param i the new main library number
	 */
	public void setMainLibraryNumber(int i) {
		mainLibraryNumber = i;
	}


	/**
	 * Sets the shelf list key number.
	 *
	 * @param i the new shelf list key number
	 */
	public void setShelfListKeyNumber(int i) {
		shelfListKeyNumber = i;
		getKey().setHeadingNumber(i);
	}

	/**
	 * Sets the type code.
	 *
	 * @param c the new type code
	 */
	public void setTypeCode(char c) {
		typeCode = c;
	}


	/* (non-Javadoc)
	 * @see Descriptor#getAccessPointClass()
	 */
	public Class getAccessPointClass() {
		return SHLF_LIST_ACS_PNT.class;
	}


	/* (non-Javadoc)
	 * @see Descriptor#getCategory()
	 */
	public int getCategory() {
		return 14;
	}


	/* (non-Javadoc)
	 * @see Descriptor#getCorrelationValues()
	 */
	public CorrelationValues getCorrelationValues() {
		return new CorrelationValues(
				(short)getTypeCode(),
				CorrelationValues.UNDEFINED,
				CorrelationValues.UNDEFINED);
	}

	/* (non-Javadoc)
	 * @see Descriptor#generateNewKey()
	 */
	/*
	  * The default implementation sets key.headingNumber to the new stringValue.
	  * SHLF_LIST differs from most descriptors in that the key field is not
	  * used (since there are no user views)
	*/
	@Override
	public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
		super.generateNewKey(session);
		setShelfListKeyNumber(getKey().getHeadingNumber());
	}

	/**
	 * Gets the dao.
	 *
	 * @return the dao
	 */
	public AbstractDAO getDAO() {
		return new ShelfListDAO();
	}

	/* (non-Javadoc)
	 * @see Descriptor#getDefaultBrowseKey()
	 */
	public String getDefaultBrowseKey() {
		return "28P30";
	}

	/* (non-Javadoc)
	 * @see Descriptor#getNextNumberKeyFieldCode()
	 */
	public String getNextNumberKeyFieldCode() {
		return "FL";
	}

	/* (non-Javadoc)
	 * @see Descriptor#getReferenceClass(java.lang.Class)
	 */
	public Class getReferenceClass(Class targetClazz) {
		return null;
	}

	/* (non-Javadoc)
	 * @see Descriptor#setCorrelationValues(CorrelationValues)
	 */
	public void setCorrelationValues(CorrelationValues v) {
		setTypeCode((char)v.getValue(1));
	}

	/* (non-Javadoc)
	 * @see Descriptor#getSortFormParameters()
	 */
	public SortFormParameters getSortFormParameters() {
		return new SortFormParameters(200, (int)getTypeCode(), 0, 0, 0);
	}

	/* (non-Javadoc)
	 * @see Descriptor#isCanTransfer()
	 */
	public boolean isCanTransfer() {
		return false;
	}


	/* (non-Javadoc)
	 * @see Descriptor#getHeadingNumberSearchIndexKey()
	 */
	public String getHeadingNumberSearchIndexKey() {
		return "232P";
	}


	/* (non-Javadoc)
	 * @see Descriptor#getLockingEntityType()
	 */
	public String getLockingEntityType() {
		return "FL";
	}


}
