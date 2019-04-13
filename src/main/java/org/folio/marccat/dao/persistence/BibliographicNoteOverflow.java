package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.business.common.PersistentObjectWithView;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.DAOBibliographicNotesOverflow;
import org.folio.marccat.dao.SystemNextNumberDAO;

import java.io.Serializable;

/**
 * Class comment
 *
 * @author hansv
 */
public class BibliographicNoteOverflow implements PersistentObjectWithView, Serializable {

  private String displayValue = null;
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
  public String getDisplayValue() {
    return displayValue;
  }

  /**
   * @param string
   */
  public void setDisplayValue(String string) {
    displayValue = string;
  }


  public boolean equals(Object obj) {
    if (!(obj instanceof BibliographicNoteOverflow))
      return false;
    BibliographicNoteOverflow noteOverflow = (BibliographicNoteOverflow) obj;
    return (noteOverflow.getBibItemNumber() == getBibItemNumber())
      && (noteOverflow.getNoteNbr() == getNoteNbr())
      && (noteOverflow.getNoteOverflowNumber() == getNoteOverflowNumber())
      && (noteOverflow.getUserViewString() == getUserViewString());
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return bibItemNumber + noteNbr + noteOverflowNumber + userViewString.hashCode();
  }

  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public int getBibItemNumber() {
    return bibItemNumber;
  }

  /**
   * @param i
   * @throws
   * @see
   * @since 1.0
   */
  public void setBibItemNumber(int i) {
    bibItemNumber = i;
  }

  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public int getNoteNbr() {
    return noteNbr;
  }

  /**
   * @param i
   * @throws
   * @see
   * @since 1.0
   */
  public void setNoteNbr(int i) {
    noteNbr = i;
  }

  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public int getNoteOverflowNumber() {
    return noteOverflowNumber;
  }

  /**
   * @param i
   * @throws
   * @see
   * @since 1.0
   */
  public void setNoteOverflowNumber(int i) {
    noteOverflowNumber = i;
  }

  /**
   * @return
   * @throws
   * @see
   * @since 1.0
   */
  public String getUserViewString() {
    return userViewString;
  }

  /**
   * @param string
   * @throws
   * @see
   * @since 1.0
   */
  public void setUserViewString(String string) {
    userViewString = string;
  }

  public void generateNewKey(final Session session) throws HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setNoteOverflowNumber(dao.getNextNumber("BO", session));
  }

  public boolean onSave(Session arg0) throws CallbackException {
    if (persistenceState != null) {
      return persistenceState.onSave(arg0);
    } else {
      return true;
    }
  }

  public boolean onDelete(Session arg0) throws CallbackException {
    if (persistenceState != null) {
      return persistenceState.onDelete(arg0);
    } else {
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
    } else {
      return true;
    }
  }

  public void markChanged() {
    if (persistenceState != null) {
      persistenceState.markChanged();
    }
  }


  public void markDeleted() {
    if (persistenceState != null) {
      persistenceState.markDeleted();
    }
  }


  public void markNew() {
    if (persistenceState != null) {
      persistenceState.markNew();
    }
  }


  public void markUnchanged() {
    if (persistenceState != null) {
      persistenceState.markUnchanged();
    }
  }

  public void evict() {
    if (persistenceState != null) {
      persistenceState.evict(this);
    }
  }

  public int getUpdateStatus() {
    if (persistenceState == null) {
      return -1;
    } else {
      return persistenceState.getUpdateStatus();
    }
  }

  public void setUpdateStatus(int i) {
    if (persistenceState != null) {
      persistenceState.setUpdateStatus(i);
    }
  }

  public boolean isChanged() {
    if (persistenceState == null) {
      return false;
    } else {
      return persistenceState.isChanged();
    }
  }


  public boolean isDeleted() {
    if (persistenceState == null) {
      return false;
    } else {
      return persistenceState.isDeleted();
    }
  }


  public boolean isNew() {
    if (persistenceState == null) {
      return false;
    } else {
      return persistenceState.isNew();
    }
  }


  public boolean isRemoved() {
    if (persistenceState == null) {
      return false;
    } else {
      return persistenceState.isRemoved();
    }
  }

  public AbstractDAO getDAO() {
    return new DAOBibliographicNotesOverflow();
  }

}
