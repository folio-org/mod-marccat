package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.PUBL_HDG;
import java.sql.SQLException;
import java.util.List;

/**
 * Manages headings in the PUBL_HDG table.
 *
 * @author paulm
 * @author carment
 */
public class PublisherDescriptorDAO extends DescriptorDAO {

  /**
   * Gets the persistent class.
   *
   * @return the persistent class
   */
  public Class getPersistentClass() {
    return PUBL_HDG.class;
  }

  /**
   * Gets the matching heading.
   *
   * @param descriptor the descriptor
   * @param session    the session
   * @return the matching heading
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public PUBL_HDG getMatchingHeading(final Descriptor descriptor, final Session session)
    throws HibernateException, SQLException {
    final PUBL_HDG publisher = (PUBL_HDG) descriptor;
    publisher.setSortForm(calculateSortForm(descriptor, session));
    final List<PUBL_HDG> l = session.find("from "
        + getPersistentClass().getName()
        + " as c "
        + " where c.nameStringText = ?"
        + " and c.placeStringText = ? "
        + " and c.indexingLanguage = ? "
        + " and c.accessPointLanguage = ?"
        + " and c.key.userViewString = ? ",
      new Object[]{
        publisher.getNameStringText(),
        publisher.getPlaceStringText(),
        publisher.getIndexingLanguage(),
        publisher.getAccessPointLanguage(),
        publisher.getUserViewString()},
      new Type[]{
        Hibernate.STRING,
        Hibernate.STRING,
        Hibernate.INTEGER,
        Hibernate.INTEGER,
        Hibernate.STRING});
    if (!l.isEmpty()) {
      return l.get(0);
    } else {
      return null;
    }

  }

  /**
   * Checks if is matching another heading.
   *
   * @param descriptor the descriptor
   * @param session    the session
   * @return true, if is matching another heading
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean isMatchingAnotherHeading(final Descriptor descriptor, final Session session)
    throws HibernateException {
    final PUBL_HDG publisher = (PUBL_HDG) descriptor;
    final List<Integer> publisherList = session.find(
      "select count(*) from "
        + getPersistentClass().getName()
        + " as c "
        + " where c.nameStringText = ?"
        + " and c.placeStringText = ? "
        + " and c.indexingLanguage = ? "
        + " and c.accessPointLanguage = ?"
        + " and c.key.userViewString = ? "
        + " and c.key.headingNumber <> ?",
      new Object[]{
        publisher.getNameStringText(),
        publisher.getPlaceStringText(),
        publisher.getIndexingLanguage(),
        publisher.getAccessPointLanguage(),
        publisher.getUserViewString(),
        descriptor.getKey().getHeadingNumber()},
      new Type[]{Hibernate.STRING,
        Hibernate.STRING,
        Hibernate.INTEGER,
        Hibernate.INTEGER,
        Hibernate.STRING,
        Hibernate.INTEGER
      });
    return publisherList.get(0) > 0;

  }




  /**
   * Gets the document count.
   *
   * @param descriptor    the descriptor
   * @param searchingView the searching view
   * @param session       the session
   * @return the document count
   * @throws HibernateException the hibernate exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public int getDocCount(final Descriptor descriptor, int searchingView, final Session session)
    throws HibernateException {
    if (searchingView == View.ANY) {
      List<Integer> countList =
        session.find(
          "select count(*) from PublisherAccessPoint as a, PUBL_TAG as b "
            + " where a.headingNumber = b.publisherTagNumber "
            + " and b.publisherHeadingNumber = ? ",
          new Object[]{
            descriptor.getHeadingNumber()},
          new Type[]{
            Hibernate.INTEGER});
      return countList.get(0);
    } else {
      List<Integer> countList =
        session.find(
          "select count(*) from PublisherAccessPoint as a, PUBL_TAG as b "
            + " where a.headingNumber = b.publisherTagNumber "
            + " and b.publisherHeadingNumber = ? "
            + " and b.userViewString = '" + View.makeSingleViewString(searchingView) + "'",
          new Object[]{
            descriptor.getHeadingNumber()},
          new Type[]{
            Hibernate.INTEGER});
      return countList.get(0);
    }

  }

}
