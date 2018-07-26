package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAOCodeTable;

import java.io.Serializable;

public abstract class CodeTable implements Persistence 
{
	private char obsoleteIndicator;
	private String shortText;
	private String longText;
	private String language;
	private int sequence;	
	
	public CodeTable() {
		super();
	}
	
	public abstract int getNextNumber() throws DataAccessException;

	public String getLongText() {
		return longText;
	}

	public char isObsoleteIndicator() {
		return obsoleteIndicator;
	}

	public String getShortText() {
		return shortText;
	}

	public void setLongText(String string) {
		longText = string;
	}

	public void setObsoleteIndicator(char b) {
		obsoleteIndicator = b;
	}

	public void setShortText(String string) {
		shortText = string;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int s) {
		sequence = s;
	}

	abstract public String getCodeString();

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String string) {
		language = string;
	}


	//-----------------------------
	// Persistence and LifeCycle
	//-----------------------------
	private PersistenceState persistenceState = new PersistenceState();
	
	public void evict() throws DataAccessException {
		persistenceState.evict(this);
	}

	public void generateNewKey() throws DataAccessException {		
	}

	public AbstractDAO getDAO() {
		return new DAOCodeTable();
	}

	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
	}

	public boolean isChanged() {
		return persistenceState.isChanged();
	}

	public boolean isDeleted() {
		return persistenceState.isDeleted();
	}

	public boolean isNew() {
		return persistenceState.isNew();
	}

	public void markChanged() {
		persistenceState.markChanged();
	}

	public void markDeleted() {
		persistenceState.markDeleted();
	}

	public void markNew() {
		persistenceState.markNew();
	}

	public void markUnchanged() {
		persistenceState.markUnchanged();
	}

	public void setUpdateStatus(int i) {
		persistenceState.setUpdateStatus(i);
	}

	public boolean onDelete(Session arg0) throws CallbackException {
		return persistenceState.onDelete(arg0);
	}

	public void onLoad(Session arg0, Serializable arg1) {
		persistenceState.onLoad(arg0,arg1);
	}

	public boolean onSave(Session arg0) throws CallbackException {
		return persistenceState.onSave(arg0);
	}

	public boolean onUpdate(Session arg0) throws CallbackException {
		return persistenceState.onUpdate(arg0);
	} 
	//-------------------------------
	// END Persistence and LifeCycle
	//-------------------------------

	public abstract void setExternalCode(Object extCode);

	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + sequence;
		return result;
	}

	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CodeTable other = (CodeTable) obj;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
        return sequence == other.sequence;
    }
}