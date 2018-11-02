package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.dao.common.HibernateUtil;
import org.folio.marccat.dao.persistence.BibliographicNote;
import org.folio.marccat.dao.persistence.BibliographicNoteTag;

import java.util.ArrayList;
import java.util.List;

public class DAODigital extends HibernateUtil {
  private static final Log logger = LogFactory.getLog(DAODigital.class);

  public DAODigital() {
    super();
  }

  public int countRLTSP(int amicusNumber) throws DataAccessException {
    int result = 0;
    try {
      Session s = currentSession();
      Query q = s.createQuery("select count(*) from BibliographicRelationship as br where br.relationTypeCode in (1,2,5) and br.bibItemNumber = " + amicusNumber);
      q.setMaxResults(1);
      result = ((Integer) q.list().get(0)).intValue();

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    logger.debug("Risultato count su RLTSP : " + result);
    return result;
  }

  public List getBibliographicNotes856(int id, int userView, String lingua) throws HibernateException, DataAccessException {
    List noteTags = new ArrayList();

    List multiView = currentSession().find(
      "from BibliographicNote t "
        + "where t.bibItemNumber = ? and substr(t.userViewString, ?, 1) = '1'"
        + " and t.noteType = 346",
      new Object[]{new Integer(id), new Integer(userView)},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

    List singleView = isolateViewForList(multiView, userView);

    for (int i = 0; i < singleView.size(); i++) {
      noteTags.add(i, new BibliographicNoteTag((BibliographicNote) singleView.get(i)));
      BibliographicNoteTag currentNote = ((BibliographicNoteTag) noteTags.get(i));
      currentNote.markUnchanged();
      currentNote.setOverflowList(currentNote.getOverflowList(userView));

      if (!lingua.equals("")) {
        currentNote.setNoteStandard(currentNote.loadNoteStandard(userView, lingua));

        if (currentNote.isStandardNoteType() && currentNote.getNote().getContent() != null/*.indexOf("\u001f")==-1*/) {
          if (currentNote.getNote().getContent().indexOf("\u001f") == -1) {
            currentNote.getNote().setContent("\u001f" + "a" + currentNote.getNote().getContent());
            currentNote.getNote().markUnchanged();
            currentNote.markUnchanged();
          }
        } else if (currentNote.isStandardNoteType() && currentNote.getNote().getContent() == null/*.indexOf("\u001f")==-1*/) {
          currentNote.getNote().setContent("\u001f" + "a" + "");
          currentNote.getNote().markUnchanged();
          currentNote.markUnchanged();
        }
      }
    }
    return noteTags;
  }
}
