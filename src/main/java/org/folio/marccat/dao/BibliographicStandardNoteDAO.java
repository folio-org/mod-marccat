package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.dao.persistence.StandardNoteAccessPoint;
import org.folio.marccat.dao.persistence.T_STD_NTE_TYP;

import java.util.List;
import java.util.Objects;

/**
 * Data access object to standard note.
 *
 * @author nbianchini
 */
public class BibliographicStandardNoteDAO extends AbstractDAO {

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

  public Avp<String> getSTDDisplayString(final int code, final String language, final Session session) throws HibernateException {

    List<T_STD_NTE_TYP> l = null;
    Query q = session.createQuery("select distinct ct from T_STD_NTE_TYP  as ct where ct.code =" + code + " and ct.language ='" + language + "'");
    q.setMaxResults(1);
    l = q.list();
    if (l.stream().anyMatch(Objects::nonNull)) {
      return l.stream().findFirst().map(standardNoteType -> ((Avp<String>) new Avp(Integer.toString(standardNoteType.getCode()), standardNoteType.getLongText()))).get();
    } else
      return null;
  }

}
