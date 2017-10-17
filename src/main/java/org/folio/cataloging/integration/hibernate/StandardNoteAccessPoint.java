/*
 * (c) LibriCore
 * 
 * Created on Dec 6, 2004
 * 
 * S_CAS_CACHE.java
 */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;
import librisuite.business.cataloguing.bibliographic.DAOBibliographicNoteTag;
import librisuite.business.cataloguing.bibliographic.DAOBibliographicStandardNote;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import com.libricore.librisuite.common.HibernateUtil;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class StandardNoteAccessPoint implements Persistence {
	private PersistenceState persistenceState = new PersistenceState();
	private static final DAOBibliographicStandardNote dao =	new DAOBibliographicStandardNote();
	
    protected short typeCode = -1;
    private int bibItemNumber = -1;
	private String userViewString;
	private int noteNbr =-1;
    
	
	public StandardNoteAccessPoint(int bibItemNumber) {
		setBibItemNumber(bibItemNumber);
		
	}
	public StandardNoteAccessPoint() {
		
	}
    public short getTypeCode() {
		return typeCode;
	}


	public void setTypeCode(short typeCode) {
		this.typeCode = typeCode;
	}



	public int getBibItemNumber() {
		return bibItemNumber;
	}



	public void setBibItemNumber(int bibItemNumber) {
		this.bibItemNumber = bibItemNumber;
	}



	public String getUserViewString() {
		return userViewString;
	}



	public void setUserViewString(String userViewString) {
		this.userViewString = userViewString;
	}



	public int getNoteNbr() {
		return noteNbr;
	}



	public void setNoteNbr(int noteNbr) {
		this.noteNbr = noteNbr;
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

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bibItemNumber;
		result = prime * result + noteNbr;
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StandardNoteAccessPoint other = (StandardNoteAccessPoint) obj;
		if (bibItemNumber != other.bibItemNumber)
			return false;
		if (noteNbr != other.noteNbr)
			return false;
		return true;
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
	
	


}
