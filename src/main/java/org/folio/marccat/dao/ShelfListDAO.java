package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.SHLF_LIST;
import org.folio.marccat.dao.persistence.SHLF_LIST_ACS_PNT;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ReferentialIntegrityException;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Manages headings in the SHLF_LIST table
 *
 * @author carment
 * @since 1.0
 */
public class ShelfListDAO extends DescriptorDAO {

  /**
   * Gets the persistent class.
   *
   * @return the persistent class
   */
  public Class getPersistentClass() {
    return SHLF_LIST.class;
  }

  /**
   * Load.
   *
   * @param shelfListKeyNumber the shelf list key number
   * @param session            the session
   * @return the shlfList
   * @throws HibernateException the hibernate exception
   */
  public SHLF_LIST load(final int shelfListKeyNumber, final Session session)
    throws HibernateException {
    return (SHLF_LIST) session.get(SHLF_LIST.class, shelfListKeyNumber);
  }

  /**
   * Load the access point of shlfList.
   *
   * @param shelfListKeyNumber the shelf list key number
   * @param session            the session
   * @return the shlf list acs pnt
   * @throws HibernateException the hibernate exception
   */
  public SHLF_LIST_ACS_PNT loadAccessPoint(final int shelfListKeyNumber, final Session session)
    throws HibernateException {
    return (SHLF_LIST_ACS_PNT) session.get(SHLF_LIST_ACS_PNT.class, shelfListKeyNumber);
  }

  /**
   * Gets the shelf list.
   *
   * @param shelfToSearch the shlfList to search
   * @param session       the session
   * @return the shelf list
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  public SHLF_LIST getShelfList(final SHLF_LIST shelfToSearch, final Session session)
    throws HibernateException, SQLException {
    final String shelfListSortForm = calculateSortForm(shelfToSearch, session);
    final List<SHLF_LIST> shelfList = session.find("from SHLF_LIST as sl " +
        "where sl.sortForm = ? "
        + "AND sl.typeCode = ? "
        + "AND sl.mainLibraryNumber = ? ",
      new Object[]{
        shelfListSortForm,
        shelfToSearch.getTypeCode(),
        shelfToSearch.getMainLibraryNumber()},
      new Type[]{
        Hibernate.STRING,
        Hibernate.CHARACTER,
        Hibernate.INTEGER});

    final Optional<SHLF_LIST> firstElement = shelfList.stream().filter(Objects::nonNull).findFirst();
    return firstElement.isPresent() ? firstElement.get() : null;
  }


  /**
   * Gets the document count.
   *
   * @param d               the d
   * @param cataloguingView the cataloguing view
   * @param session         the session
   * @return the doc count
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public int getDocCount(final Descriptor d, final int cataloguingView, final Session session) throws HibernateException {
    List<Integer> countList = session.find(
      " select count(*) from "
        + d.getAccessPointClass().getName()
        + " as apf "
        + " where apf.shelfListKeyNumber = ? and apf.mainLibraryNumber=?",
      new Object[]{
        ((SHLF_LIST) d).getShelfListKeyNumber(),
        ((SHLF_LIST) d).getMainLibraryNumber()},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

    final Optional<Integer> firstElement = countList.stream().filter(Objects::nonNull).findFirst();
    return firstElement.isPresent() ? firstElement.get() : 0;
  }

  /**
   * Returns the first n rows having sort form > term.
   *
   * @param operator      the operator
   * @param direction     the direction
   * @param term          the term
   * @param filter        the filter
   * @param searchingView the searching view
   * @param count         the count
   * @param session       the session
   * @return the headings by sort form
   * @throws DataAccessException the data access exception
   * @throws HibernateException  the hibernate exception
   */
  @Override
  public List<Descriptor> getHeadingsBySortform(final String operator, final String direction,
                                                final String term, final String filter, final int searchingView,
                                                final int count, final Session session)
    throws HibernateException {
    final Query q = session.createQuery("from " + getPersistentClass().getName()
      + " as hdg where hdg.sortForm " + operator + " :term "
      + filter + " order by hdg.sortForm "
      + direction);
    q.setString("term", term);
    q.setMaxResults(count);
    return (List<Descriptor>) q.list();
  }


  /**
   * Gets the matching heading(SHLF_LIST).
   *
   * @param descriptor the descriptor
   * @param session    the session
   * @return the matching heading
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public Descriptor getMatchingHeading(final Descriptor descriptor, final Session session)
    throws HibernateException, SQLException {
    final SHLF_LIST shelf = (SHLF_LIST) descriptor;
    descriptor.setSortForm(calculateSortForm(descriptor, session));
    final List<SHLF_LIST> shelfList = session.find("from "
        + getPersistentClass().getName()
        + " as c "
        + " where upper(c.stringText) = ? and c.mainLibraryNumber = ? "
        + " and c.typeCode = ? ",
      new Object[]{
        shelf.getStringText().toUpperCase(),
        shelf.getMainLibraryNumber(),
        shelf.getTypeCode()},
      new Type[]{
        Hibernate.STRING,
        Hibernate.INTEGER,
        Hibernate.CHARACTER});
    final Optional<SHLF_LIST> firstElement = shelfList.stream().filter(Objects::nonNull).findFirst();
    return firstElement.isPresent() ? firstElement.get() : null;
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
   * Load the heading(SHLF_LIST) by heading number
   *
   * @param headingNumber   the heading number
   * @param cataloguingView the cataloguing view
   * @return the descriptor
   * @throws DataAccessException the data access exception
   */
  @Override
  public Descriptor load(int headingNumber, int cataloguingView, final Session session) throws DataAccessException, HibernateException {
    return load(headingNumber, session);
  }


  /**
   * Persist heading by status
   *
   * @param descriptor the descriptor
   * @param session    the session
   * @throws HibernateException the hibernate exception
   */
  @Override
  public void persist(final Descriptor descriptor, final Session session) throws HibernateException {
    if (descriptor.isNew()) {
      ((SHLF_LIST) descriptor).setShelfListKeyNumber(
        new SystemNextNumberDAO().getNextNumber(descriptor.getNextNumberKeyFieldCode(), session));
    }
    persistByStatus(descriptor, session);
  }

  /**
   * Delete the SHLF_LIST
   *
   * @param p       the p
   * @param session the session
   * @throws ReferentialIntegrityException the referential integrity exception
   * @throws HibernateException            the hibernate exception
   */
  @Override
  public void delete(final Persistence p, final Session session) throws ReferentialIntegrityException, HibernateException {
    final SHLF_LIST descriptor = ((SHLF_LIST) p);
    final List<Integer> countList = session.find("select count(*) from " + descriptor.getAccessPointClass().getName() + " as a where a.shelfListKeyNumber = ?",
      new Object[]{
        descriptor.getShelfListKeyNumber()},
      new Type[]{Hibernate.INTEGER});
    final Optional<Integer> firstElement = countList.stream().filter(Objects::nonNull).findFirst();
    if (firstElement.isPresent() && firstElement.get() > 0)
      throw new ReferentialIntegrityException(
        descriptor.getAccessPointClass().getName(),
        descriptor.getClass().getName());
    p.markDeleted();
    persistByStatus(p, session);
  }


}
