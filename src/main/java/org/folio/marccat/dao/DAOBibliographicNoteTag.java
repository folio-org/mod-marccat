/*
 * (c) LibriCore
 *
 * Created on Dec 22, 2004
 *
 * DAOPublisherTag.java
 */
package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.UpdateStatus;
import org.folio.marccat.dao.persistence.BibliographicNote;
import org.folio.marccat.dao.persistence.BibliographicNoteOverflow;
import org.folio.marccat.dao.persistence.BibliographicNoteTag;
import org.folio.marccat.dao.persistence.StandardNoteAccessPoint;

import java.util.Iterator;

/**
 * @author hansv
 * @version $Revision: 1.4 $ $Date: 2005/12/21 08:30:32 $
 * @since 1.0
 */
//TODO refactor!
public class DAOBibliographicNoteTag extends AbstractDAO {
  private static final Log logger = LogFactory.getLog(BibliographicNoteTag.class);

  /* (non-Javadoc)
   * @see HibernateUtil#delete(librisuite.business.common.Persistence)
   */
  public void delete(Persistence po) throws DataAccessException {
    if (!(po instanceof BibliographicNoteTag)) {
      throw new IllegalArgumentException("I can only persist BibliographicNoteTag objects");
    }
    BibliographicNoteTag aNote = (BibliographicNoteTag) po;
    aNote.getNote().markDeleted();
    persistByStatus(aNote.getNote());
    if (aNote.getNoteStandard() != null) {
      aNote.getNoteStandard().markDeleted();
      persistByStatus(aNote.getNoteStandard());
    }
    Iterator iter = aNote.getOverflowList().iterator();
    BibliographicNoteOverflow overflow;
    while (iter.hasNext()) {
      overflow = (BibliographicNoteOverflow) iter.next();
      overflow.markDeleted();
      persistByStatus(overflow);
      super.delete(overflow);
    }
    aNote.setUpdateStatus(UpdateStatus.REMOVED);
  }

  /* (non-Javadoc)
   * @see HibernateUtil#save(librisuite.business.common.Persistence)
   */
  public void save(final Persistence po, final Session session) throws DataAccessException, HibernateException {
    if (!(po instanceof BibliographicNoteTag)) {
      throw new IllegalArgumentException("I can only persist BibliographicNoteTag objects");
    }

    BibliographicNoteTag aNote = (BibliographicNoteTag) po;

    Iterator iter = aNote.getOverflowList().iterator();
    BibliographicNote note = aNote.getNote();
    while (iter.hasNext()) {
      BibliographicNoteOverflow noteOverflow = (BibliographicNoteOverflow) iter.next();
      noteOverflow.setBibItemNumber(note.getItemNumber());
      noteOverflow.setUserViewString(note.getUserViewString());
      noteOverflow.setNoteNbr(note.getNoteNbr());
      if (noteOverflow.isNew()) {
        noteOverflow.generateNewKey(session);
      }
      persistByStatus(noteOverflow, session);
    }

    iter = aNote.getDeletedOverflowList().iterator();
    while (iter.hasNext()) {
      BibliographicNoteOverflow noteOverflow = (BibliographicNoteOverflow) iter.next();
      if (noteOverflow.isDeleted()) {
        persistByStatus(noteOverflow, session);
      }
    }

    aNote.getDeletedOverflowList().clear();

    // StandardNotes
    StandardNoteAccessPoint noteStandard = aNote.getNoteStandard();
    if (aNote.isStandardNoteType()) {
      noteStandard.setBibItemNumber(note.getItemNumber());
      noteStandard.setUserViewString(note.getUserViewString());
      noteStandard.setNoteNbr(note.getNoteNbr());
      /* Testo variabile RIMOZIONE CODICE SOTTOCAMPO 03/04/2009*/
      persistByStatus(noteStandard, session);
			/*if (aNote.isStandardNoteType()){
				if(note.getContent().substring(2).trim().length()>0)
				note.setContent(note.getContent().substring(2));
				else
				  note.setContent(" "); */
      //}

    }

    if (aNote.isNew()) {
      aNote.getNote().markNew();
      if (aNote.isStandardNoteType())
        aNote.getNoteStandard().markNew();

    }

    persistByStatus(note, session);

    if (aNote.isStandardNoteType())
      persistByStatus(aNote.getNoteStandard(), session);
    aNote.markUnchanged();
  }

  /* (non-Javadoc)
   * @see HibernateUtil#update(librisuite.business.common.Persistence)
   */
  public void update(final Persistence p, final Session session) throws DataAccessException {
    try {
      save(p, session);
    } catch (HibernateException e) {
      throw new DataAccessException(e);
    }
  }

}
