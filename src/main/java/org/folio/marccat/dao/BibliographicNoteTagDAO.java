package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.UpdateStatus;
import org.folio.marccat.dao.persistence.BibliographicNote;
import org.folio.marccat.dao.persistence.BibliographicNoteOverflow;
import org.folio.marccat.dao.persistence.BibliographicNoteTag;
import java.util.Iterator;

/**
 * Class to handle the notes.
 *
 * @author paulm
 * @author carment
 */

public class BibliographicNoteTagDAO extends AbstractDAO {

  /**
   * Delete a note, a note standard and the notes overflow
   *
   * @param po the po
   * @param session the session
   * @throws HibernateException the hibernate exception
   */
  @Override
  public void delete(Persistence po, final Session session) throws HibernateException {
    if (!(po instanceof BibliographicNoteTag)) {
      throw new IllegalArgumentException("I can only persist BibliographicNoteTag objects");
    }
    BibliographicNoteTag aNote = (BibliographicNoteTag) po;
    aNote.getNote().markDeleted();
    persistByStatus(aNote.getNote(), session);
    Iterator<BibliographicNoteOverflow> iter = aNote.getOverflowList().iterator();
    BibliographicNoteOverflow overflow;
    while (iter.hasNext()) {
      overflow = iter.next();
      overflow.markDeleted();
      persistByStatus(overflow, session);
    }
    aNote.setUpdateStatus(UpdateStatus.REMOVED);
  }

  /**
   * Save a note, a note standard and the notes overflow.
   *
   * @param po the po
   * @param session the session
   * @throws HibernateException the hibernate exception
   */
  @Override
  public void save(final Persistence po, final Session session) throws HibernateException {
    if (!(po instanceof BibliographicNoteTag)) {
      throw new IllegalArgumentException("I can only persist BibliographicNoteTag objects");
    }

    BibliographicNoteTag aNote = (BibliographicNoteTag) po;

    Iterator<BibliographicNoteOverflow> iter = aNote.getOverflowList().iterator();
    BibliographicNote note = aNote.getNote();
    while (iter.hasNext()) {
      BibliographicNoteOverflow noteOverflow =  iter.next();
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
      BibliographicNoteOverflow noteOverflow =  iter.next();
      if (noteOverflow.isDeleted()) {
        persistByStatus(noteOverflow, session);
      }
    }
    aNote.getDeletedOverflowList().clear();
    if (aNote.isNew()) {
      aNote.getNote().markNew();
    }
    persistByStatus(note, session);
    aNote.markUnchanged();
  }



}
