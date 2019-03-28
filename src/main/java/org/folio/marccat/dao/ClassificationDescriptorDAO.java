package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.persistence.CLSTN;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.exception.DataAccessException;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;


/**
 * Manages headings in the CLSTN table.
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public class ClassificationDescriptorDAO extends DAODescriptor {


  /**
   * Gets the persistent class.
   *
   * @return the persistent class
   */
  public Class getPersistentClass() {
    return CLSTN.class;
  }

  /**
   * Supports cross references.
   *
   * @return true, if successful
   */
  @Override
  public boolean supportsCrossReferences() {
    return false;
  }

  /**
   * Gets the matching heading(CLSTN).
   *
   * @param descriptor the descriptor
   * @param session    the session
   * @return the matching heading
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public Descriptor getMatchingHeading(final Descriptor descriptor, final Session session) throws HibernateException, SQLException {
    final CLSTN d = (CLSTN) descriptor;
    descriptor.setSortForm(calculateSortForm(descriptor, session));
    final List<CLSTN> list = session.find(
      "from "
        + getPersistentClass().getName()
        + " as c "
        + " where c.stringText = ?"
        + " and c.typeCode = ?"
        + " and c.deweyEditionNumber =?"
        + " and c.key.userViewString = ? ",
      new Object[]{
        d.getStringText(),
        d.getTypeCode(),
        d.getDeweyEditionNumber(),
        d.getUserViewString()},
      new Type[]{
        Hibernate.STRING,
        Hibernate.INTEGER,
        Hibernate.SHORT,
        Hibernate.STRING});
    return list.stream().filter(Objects::nonNull).findFirst().orElse(null);
  }

  /**
   * Checks if a heading(CLSTN) is matching another heading(CLSTN).
   *
   * @param descriptor the descriptor
   * @param session    the session
   * @return true, if is matching another heading
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean isMatchingAnotherHeading(final Descriptor descriptor, final Session session) throws HibernateException {
    final CLSTN d = (CLSTN) descriptor;
    final List<Integer> countList =
      session.find(
        "select count(*) from "
          + getPersistentClass().getName()
          + " as c "
          + " where c.stringText = ?"
          + " and c.typeCode = ?"
          + " and c.key.userViewString = ? "
          + " and c.key.headingNumber <> ?",
        new Object[]{
          d.getStringText(),
          d.getTypeCode(),
          d.getUserViewString(),
          d.getKey().getHeadingNumber()},
        new Type[]{
          Hibernate.STRING,
          Hibernate.INTEGER,
          Hibernate.STRING,
          Hibernate.INTEGER});
    return countList.stream().filter(Objects::nonNull).anyMatch(count -> count > 0);
  }

  /**
   * Gets the headings by sortform.
   *
   * @param operator      the operator
   * @param direction     the direction
   * @param term          the term
   * @param filter        the filter
   * @param searchingView the searching view
   * @param count         the count
   * @param session       the session
   * @return the headings by sortform
   * @throws DataAccessException the data access exception
   * @throws HibernateException  the hibernate exception
   */
  @Override
  public List<Descriptor> getHeadingsBySortform(final String operator, final String direction,
                                                final String term, final String filter,
                                                final int searchingView, final int count, final Session session)
    throws HibernateException {
    String viewClause = "";
    if (searchingView != View.ANY)
      viewClause = " and hdg.key.userViewString, = '" + View.makeSingleViewString(searchingView) + "' ";

    final Query q = session.createQuery("from " + getPersistentClass().getName()
      + " as hdg where hdg.sortForm " + operator
      + " :term  "
      + viewClause
      + filter + " order by hdg.sortForm " + direction);
    q.setString("term", term);
    q.setMaxResults(count);
    return (List<Descriptor>) q.list();
  }


}
