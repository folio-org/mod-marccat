package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.dao.persistence.BibliographicNoteOverflow;

import java.util.List;

/**
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class DAOBibliographicNotesOverflow extends AbstractDAO {

  @SuppressWarnings("unchecked")
  public List <BibliographicNoteOverflow> getBibNotesOverflowList(final int bibItemNumber, final int userView, final int noteNumber, final Session session) throws HibernateException {
    List <BibliographicNoteOverflow> notesOverflow = session.find(
      "from BibliographicNoteOverflow t "
        + " where t.bibItemNumber = ? and "
        + " substr(t.userViewString, ?, 1) = '1' and "
        + " t.noteNbr = ?"
        + " order by t.noteOverflowNumber",
      new Object[]{
        bibItemNumber,
        userView,
        noteNumber},
      new Type[]{
        Hibernate.INTEGER,
        Hibernate.INTEGER,
        Hibernate.INTEGER});

    return notesOverflow;

  }

}
