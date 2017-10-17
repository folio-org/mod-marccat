package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.PersistenceState;
import librisuite.business.searching.DAOPublisher;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import com.libricore.librisuite.common.HibernateUtil;

public class PublCdeHdg implements Persistence {
	
	private static final long serialVersionUID = -4706609571988909462L;

	private PersistenceState persistenceState = new PersistenceState();
	
	static DAOPublisher dao = new DAOPublisher();
	
	private String publisherCode;
	private String hdrNumber;
		
	public PublCdeHdg(){
		super();
	}
	
	public PublCdeHdg(String codEdi, String hdgNbr)
	{
		setPublisherCode(codEdi);
		setHdrNumber(hdgNbr);
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((hdrNumber == null) ? 0 : hdrNumber.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PublCdeHdg other = (PublCdeHdg) obj;
		if (hdrNumber == null) {
			if (other.hdrNumber != null)
				return false;
		} else if (!hdrNumber.equals(other.hdrNumber))
			return false;
		return true;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public PersistenceState getPersistenceState() {
		return persistenceState;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setPersistenceState(PersistenceState state) {
		persistenceState = state;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void evict(Object obj) throws DataAccessException {
		persistenceState.evict(obj);
	}

	public void evict() throws DataAccessException {
		evict((Object)this);
	}
		
	/**
	 * 
	 * @since 1.0
	 */
	public HibernateUtil getDAO() {
		return dao;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isChanged() {
		return persistenceState.isChanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isDeleted() {
		return persistenceState.isDeleted();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isNew() {
		return persistenceState.isNew();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isRemoved() {
		return persistenceState.isRemoved();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markChanged() {
		persistenceState.markChanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markDeleted() {
		persistenceState.markDeleted();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markNew() {
		persistenceState.markNew();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markUnchanged() {
		persistenceState.markUnchanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onDelete(Session arg0) throws CallbackException {
		return persistenceState.onDelete(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void onLoad(Session arg0, Serializable arg1) {
		persistenceState.onLoad(arg0, arg1);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onSave(Session arg0) throws CallbackException {
		return persistenceState.onSave(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onUpdate(Session arg0) throws CallbackException {
		return persistenceState.onUpdate(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setUpdateStatus(int i) {
		persistenceState.setUpdateStatus(i);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.common.Persistence#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		// not applicable for this class

	}


	public String getPublisherCode() {
		return publisherCode;
	}


	public void setPublisherCode(String publisherCode) {
		this.publisherCode = publisherCode;
	}


	public String getHdrNumber() {
		return hdrNumber;
	}


	public void setHdrNumber(String hdrNumber) {
		this.hdrNumber = hdrNumber;
	}

}
