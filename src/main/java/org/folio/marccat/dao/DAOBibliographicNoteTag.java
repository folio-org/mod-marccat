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
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.UpdateStatus;
import org.folio.marccat.dao.persistence.BibliographicNote;
import org.folio.marccat.dao.persistence.BibliographicNoteOverflow;
import org.folio.marccat.dao.persistence.BibliographicNoteTag;
import org.folio.marccat.dao.persistence.StandardNoteAccessPoint;
import org.folio.marccat.exception.DataAccessException;

import java.util.Iterator;

/**
 * @author hansv
 * @version $Revision: 1.4 $ $Date: 2005/12/21 08:30:32 $
 * @since 1.0
 */

public class DAOBibliographicNoteTag extends AbstractDAO {

  public void delete(Persistence po, final Session session) throws DataAccessException, HibernateException {
    if (!(po instanceof BibliographicNoteTag)) {
      throw new IllegalArgumentException("I can only persist BibliographicNoteTag objects");
    }
    BibliographicNoteTag aNote = (BibliographicNoteTag) po;
    aNote.getNote().markDeleted();
    persistByStatus(aNote.getNote(), session);
    if (aNote.getNoteStandard() != null) {
      aNote.getNoteStandard().markDeleted();
      persistByStatus(aNote.getNoteStandard(), session);
    }
    Iterator iter = aNote.getOverflowList().iterator();
    BibliographicNoteOverflow overflow;
    while (iter.hasNext()) {
      overflow = (BibliographicNoteOverflow) iter.next();
      overflow.markDeleted();
      persistByStatus(overflow, session);
    }
    aNote.setUpdateStatus(UpdateStatus.REMOVED);
  }

  /* (non-Javadoc)
   * @see HibernateUtil#save(librisuite.business.common.Persistence)
   */
  @Override
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
  @Override
  public void update(final Persistence p, final Session session) throws DataAccessException {
    try {
      save(p, session);
    } catch (HibernateException e) {
      throw new DataAccessException(e);
    }
  }

}
