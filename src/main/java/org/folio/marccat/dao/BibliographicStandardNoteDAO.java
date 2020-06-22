package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.dao.persistence.StandardNoteAccessPoint;
import org.folio.marccat.dao.persistence.T_STD_NTE_TYP;
import java.util.List;
import java.util.Objects;
import java.util.Optional;



/**
 * Data access object to standard note.
 *
 * @author nbianchini
 */
public class BibliographicStandardNoteDAO extends AbstractDAO {

  /**
   * Gets the bib note stardard.
   *
   * @param bibItemNumber the bib item number
   * @param userView the user view
   * @param noteNumber the note number
   * @param session the session
   * @return the bib note stardard
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  public StandardNoteAccessPoint getBibNoteStardard(
    final int bibItemNumber,
    final int userView,
    final int noteNumber, final Session session) throws HibernateException {

    List<StandardNoteAccessPoint> notesStandard = session.find("from StandardNoteAccessPoint t "
        + " where t.bibItemNumber = ? and "
        + " substr(t.userViewString, ?, 1) = '1' and "
        + " t.noteNbr = ?",
      new Object[]{
        bibItemNumber,
        userView,
        noteNumber},
      new Type[]{
        Hibernate.INTEGER,
        Hibernate.INTEGER,
        Hibernate.INTEGER});
    return notesStandard.stream().filter(Objects::nonNull).findFirst().orElse(null);
  }

  /**
   * Gets the STD display string.
   *
   * @param code the code
   * @param language the language
   * @param session the session
   * @return the STD display string
   * @throws HibernateException the hibernate exception
   */
  public Avp<String> getSTDDisplayString(final int code, final String language, final Session session) throws HibernateException {

    List <T_STD_NTE_TYP> l;
    Query q = session.createQuery("select distinct ct from T_STD_NTE_TYP  as ct where ct.code =" + code + " and ct.language ='" + language + "'");
    q.setMaxResults(1);
    l = q.list();
    if (l.stream().anyMatch(Objects::nonNull)) {
      Optional <T_STD_NTE_TYP> firstElement = l.stream().findFirst();
      if (firstElement.isPresent()) {
        T_STD_NTE_TYP standardNoteType = firstElement.get();
        return new Avp<>(Integer.toString(standardNoteType.getCode()), standardNoteType.getLongText());
      }
    }
    return null;

  }
}
