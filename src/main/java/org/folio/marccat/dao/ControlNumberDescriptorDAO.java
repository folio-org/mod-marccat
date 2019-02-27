package org.folio.marccat.dao;


import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.persistence.CNTL_NBR;
import org.folio.marccat.dao.persistence.Descriptor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.folio.marccat.config.log.Global.EMPTY_STRING;

/**
 * Manages headings in the CNTL_NBR table for NTN index.
 *
 * @author paulm
 * @author carment
 */
public class ControlNumberDescriptorDAO extends DAODescriptor {

  /**
   * Gets the persistent class.
   *
   * @return the persistent class
   */
  public Class getPersistentClass() {
    return CNTL_NBR.class;
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
   * Gets the matching heading.
   *
   * @param descriptor the descriptor
   * @param session    the session
   * @return the matching heading
   * @throws HibernateException the hibernate exception
   */
  @Override
  public Descriptor getMatchingHeading(final Descriptor descriptor, final Session session)
    throws HibernateException {
    CNTL_NBR controlNumber = (CNTL_NBR) descriptor;
    List controlNumberList = session.find(
      "from " + getPersistentClass().getName() + " as c "
        + " where c.stringText = ?"
        + " and c.typeCode = ?"
        + " and c.key.userViewString = ? ",
      new Object[]{
        controlNumber.getStringText(),
        controlNumber.getTypeCode(),
        controlNumber.getUserViewString()},
      new Type[]{
        Hibernate.STRING,
        Hibernate.INTEGER,
        Hibernate.STRING});
    final Optional<CNTL_NBR> firstElement = controlNumberList.stream().filter(Objects::nonNull).findFirst();
    return firstElement.isPresent() ? firstElement.get() : null;
  }

  /**
   * The sort forms included "extra" information to help
   * in identifying uniqueness (some headings added a code for "language of
   * access point" so that otherwise identical headings could be
   * differentiated if they had different languages). Now, the only example I
   * can think of is the Dewey Decimal classification where the sort form
   * includes the Dewey Edition Number so that identical numbers from
   * different editions will have different sortforms.
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
    CNTL_NBR controlNumber = (CNTL_NBR) descriptor;
    List<Integer> countList = session.find(
      "select count(*) from "
        + getPersistentClass().getName()
        + " as c "
        + " where  c.stringText = ? "
        + " and c.typeCode = ?"
        + " and c.key.userViewString = ?"
        + " and c.key.headingNumber <> ?",
      new Object[]{
        controlNumber.getStringText(),
        controlNumber.getTypeCode(),
        controlNumber.getUserViewString(),
        controlNumber.getKey().getHeadingNumber()},
      new Type[]{Hibernate.STRING,
        Hibernate.INTEGER,
        Hibernate.STRING, Hibernate.INTEGER});
    return countList.stream().filter(Objects::nonNull).anyMatch(count -> count > 0);

  }

  /**
   * Gets the doc count.
   *
   * @param descriptor    the descriptor
   * @param searchingView the searching view
   * @param session       the session
   * @return the count of the records
   * @throws HibernateException the hibernate exception
   */
  @Override
  public int getDocCount(final Descriptor descriptor, final int searchingView, final Session session)
    throws HibernateException {
    int count = 0;
    String viewClause = EMPTY_STRING;
    final CNTL_NBR controlNumber = (CNTL_NBR) descriptor;
    if (controlNumber.getTypeCode() == 10) {
      if (searchingView != View.ANY) {
        viewClause = " and title.userViewString, = '" + View.makeSingleViewString(searchingView) + "' ";
      }
      final Query q = session.createQuery("select count(*) from TitleAccessPoint as title " +
        " where title.seriesIssnHeadingNumber = :headingNumber " +
        viewClause);
      q.setInteger("headingNumber", descriptor.getHeadingNumber());
      final List<Integer> countList = q.list();
      count = countList.get(0);
    }
    count += super.getDocCount(controlNumber, searchingView, session);
    return count;
  }


}
