package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.business.common.PersistentObjectWithView;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.dao.DAOBibliographicNotesOverflow;
import org.folio.cataloging.dao.SystemNextNumberDAO;

import java.io.Serializable;

/**
 * Class comment
 * @author hansv
 */
public class BibliographicNoteOverflow implements PersistentObjectWithView, Serializable {

	private String stringText = null;
	private int bibItemNumber = -1;
	private int noteNbr = -1;
	private int noteOverflowNumber = -1;
	private String userViewString = "0000000000000000";	
	private PersistenceState persistenceState = new PersistenceState();

	public BibliographicNoteOverflow() {		
		super();
	}

	/**
	 * 
	 */
	public String getStringText() {
		return stringText;
	}

	/**
	 * @param string
	 */
	public void setStringText(String string) {
		stringText = string;
	}

	
	public boolean equals(Object obj) {
			if (!(obj instanceof BibliographicNoteOverflow))
				return false;
			BibliographicNoteOverflow noteOverflow = (BibliographicNoteOverflow) obj;
			return   (noteOverflow.getBibItemNumber() == getBibItemNumber())	
				  &&	(noteOverflow.getNoteNbr() == getNoteNbr())
				  &&  (noteOverflow.getNoteOverflowNumber() == getNoteOverflowNumber())
				  &&  (noteOverflow.getUserViewString() == getUserViewString());		
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
	public int hashCode() {
			return bibItemNumber + noteNbr + noteOverflowNumber + userViewString.hashCode();
		}
	
	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public int getBibItemNumber() {
		return bibItemNumber;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public int getNoteNbr() {
		return noteNbr;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public int getNoteOverflowNumber() {
		return noteOverflowNumber;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public String getUserViewString() {
		return userViewString;
	}

	/**
	 * 
	 * 
	 * @param i
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setBibItemNumber(int i) {
		bibItemNumber = i;
	}

	/**
	 * 
	 * 
	 * @param i
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setNoteNbr(int i) {
		noteNbr = i;
	}

	/**
	 * 
	 * 
	 * @param i
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setNoteOverflowNumber(int i) {
		noteOverflowNumber = i;
	}

	/**
	 * 
	 * 
	 * @param string
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setUserViewString(String string) {
		userViewString = string;
	}
	
	public void generateNewKey() throws DataAccessException {
			SystemNextNumberDAO dao = new SystemNextNumberDAO();
			setNoteOverflowNumber(dao.getNextNumber("BO"));
	}
	
	public boolean onSave(Session arg0) throws CallbackException {
			if (persistenceState != null) {
				return persistenceState.onSave(arg0);
			}
			else {
				return true;
			}
		}
		
	public boolean onDelete(Session arg0) throws CallbackException {
			if (persistenceState != null) {
				return persistenceState.onDelete(arg0);
			}
			else {
				return true;
			}
		}

	public void onLoad(Session arg0, Serializable arg1) {
		if (persistenceState != null) {
			persistenceState.onLoad(arg0, arg1);
		}
	}
	
	public boolean onUpdate(Session arg0) throws CallbackException {
			if (persistenceState != null) {
				return persistenceState.onUpdate(arg0);
			}
			else {
				return true;
			}
		}

	public void markChanged() {
		if (persistenceState != null) {
			persistenceState.markChanged();
		}
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markDeleted() {
		if (persistenceState != null) {
			persistenceState.markDeleted();
		}
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markNew() {
		if (persistenceState != null) {
			persistenceState.markNew();
		}
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markUnchanged() {
		if (persistenceState != null) {
			persistenceState.markUnchanged();
		}
	}

	public void setUpdateStatus(int i) {
		if (persistenceState != null) {
			persistenceState.setUpdateStatus(i);
		}
	}
	
	public void evict() throws DataAccessException {
		if (persistenceState != null) {
			persistenceState.evict(this);
		}
	}

	public int getUpdateStatus() {
		if (persistenceState == null) {
			return -1;
		}
		else {
		return persistenceState.getUpdateStatus();
		}
	}

	public boolean isChanged() {
			if (persistenceState == null) {
				return false;
			}
			else {
				return persistenceState.isChanged();
			}
		}

		/**
		 * 
		 * @since 1.0
		 */
		public boolean isDeleted() {
			if (persistenceState == null) {
				return false;
			}
			else {
				return persistenceState.isDeleted();
			}
		}

		/**
		 * 
		 * @since 1.0
		 */
		public boolean isNew() {
			if (persistenceState == null) {
				return false;
			}
			else {
				return persistenceState.isNew();
			}
		}
		/**
		 * 
		 * @since 1.0
		 */
		public boolean isRemoved() {
			if (persistenceState == null) {
				return false;
			}
			else {
				return persistenceState.isRemoved();
			}
		}
	
	public AbstractDAO getDAO() {
		return new DAOBibliographicNotesOverflow();
	}

}
