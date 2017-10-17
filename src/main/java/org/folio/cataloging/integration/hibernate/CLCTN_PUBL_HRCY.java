package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import com.libricore.librisuite.common.HibernateUtil;

public class CLCTN_PUBL_HRCY implements Persistence 
{
	private static final long serialVersionUID = -497668012833285975L;
	
	private int collectionCode;
    private int parentCollectionCode;
	private PersistenceState persistentState = new PersistenceState();

	public CLCTN_PUBL_HRCY(){
		super();
	}

	public CLCTN_PUBL_HRCY(int collectionCode) 
	{
		super();
		this.collectionCode = collectionCode;		
	}

	public CLCTN_PUBL_HRCY(int collectionCode, int parentCollectionCode) 
	{
		super();
		this.collectionCode = collectionCode;
		this.parentCollectionCode = parentCollectionCode;
	}

	public int getParentCollectionCode() {
		return parentCollectionCode;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + collectionCode;
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CLCTN_PUBL_HRCY other = (CLCTN_PUBL_HRCY) obj;
		if (collectionCode != other.collectionCode)
			return false;
		return true;
	}
	
	public void setParentCollectionCode(int parentCollectionCode) {
		this.parentCollectionCode = parentCollectionCode;
	}
	
	public int getCollectionCode() {
		return collectionCode;
	}
	
	public void setCollectionCode(int collectionCode) {
		this.collectionCode = collectionCode;
	}
	
	public void evict(Object obj) throws DataAccessException {
		persistentState.evict(obj);
	}

	public HibernateUtil getDAO() {
		return persistentState.getDAO();
	}

	public int getUpdateStatus() {
		return persistentState.getUpdateStatus();
	}

	public boolean isChanged() {
		return persistentState.isChanged();
	}

	public boolean isDeleted() {
		return persistentState.isDeleted();
	}

	public boolean isNew() {
		return persistentState.isNew();
	}

	public boolean isRemoved() {
		return persistentState.isRemoved();
	}

	public void markChanged() {
		persistentState.markChanged();
	}

	public void markDeleted() {
		persistentState.markDeleted();
	}

	public void markNew() {
		persistentState.markNew();
	}

	public void markUnchanged() {
		persistentState.markUnchanged();
	}

	public boolean onDelete(Session arg0) throws CallbackException {
		return persistentState.onDelete(arg0);
	}

	public void onLoad(Session arg0, Serializable arg1) {
		persistentState.onLoad(arg0, arg1);
	}

	public boolean onSave(Session arg0) throws CallbackException {
		return persistentState.onSave(arg0);
	}

	public boolean onUpdate(Session arg0) throws CallbackException {
		return persistentState.onUpdate(arg0);
	}

	public void setUpdateStatus(int i) {
		persistentState.setUpdateStatus(i);
	}

	public void evict() throws DataAccessException {
		evict((Object)this);
	}

	public void generateNewKey() throws DataAccessException {
		// not applicable for this class
	}

}