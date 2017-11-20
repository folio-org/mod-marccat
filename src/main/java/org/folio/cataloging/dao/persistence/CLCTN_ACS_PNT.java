/*
 * (c) LibriCore
 * 
 * Created on Dec 6, 2004
 * 
 * CLCTN_ACS_PNT.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import org.folio.cataloging.dao.common.HibernateUtil;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2006/02/01 14:07:37 $
 * @since 1.0
 */
public class CLCTN_ACS_PNT implements Persistence{
	private short collectionNumber;
	private int bibItemNumber;
	private PersistenceState persistentState = new PersistenceState();

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public CLCTN_ACS_PNT() {
		super();
	}

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public CLCTN_ACS_PNT(int itemNumber, short collectionNumber) {
		super();
		bibItemNumber = itemNumber;
		this.collectionNumber = collectionNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getBibItemNumber() {
		return bibItemNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public short getCollectionNumber() {
		return collectionNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setBibItemNumber(int i) {
		bibItemNumber = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setCollectionNumber(short s) {
		collectionNumber = s;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void evict(Object obj) throws DataAccessException {
		persistentState.evict(obj);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public HibernateUtil getDAO() {
		return persistentState.getDAO();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getUpdateStatus() {
		return persistentState.getUpdateStatus();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isChanged() {
		return persistentState.isChanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isDeleted() {
		return persistentState.isDeleted();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isNew() {
		return persistentState.isNew();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isRemoved() {
		return persistentState.isRemoved();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markChanged() {
		persistentState.markChanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markDeleted() {
		persistentState.markDeleted();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markNew() {
		persistentState.markNew();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markUnchanged() {
		persistentState.markUnchanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onDelete(Session arg0) throws CallbackException {
		return persistentState.onDelete(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void onLoad(Session arg0, Serializable arg1) {
		persistentState.onLoad(arg0, arg1);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onSave(Session arg0) throws CallbackException {
		return persistentState.onSave(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onUpdate(Session arg0) throws CallbackException {
		return persistentState.onUpdate(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setUpdateStatus(int i) {
		persistentState.setUpdateStatus(i);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.common.Persistence#evict()
	 */
	public void evict() throws DataAccessException {
		evict((Object)this);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.common.Persistence#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		// not applicable for this class
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (arg0 instanceof CLCTN_ACS_PNT) {
			CLCTN_ACS_PNT c = (CLCTN_ACS_PNT)arg0;
			return this.getBibItemNumber() == c.getBibItemNumber() &&
					this.getCollectionNumber() == c.collectionNumber;
		}
		else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getBibItemNumber() + getCollectionNumber();
	}

}
