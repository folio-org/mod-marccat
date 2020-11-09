package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.PUBL_HDG;
import java.util.List;


/**
 * This class implements the browse specific methods
 * special to publishers when Publisher place is being browsed.
 *
 * @author paulm
 * @author carment
 */
public class PublisherPlaceDescriptorDAO extends PublisherDescriptorDAO {


  private static final String ORDER_BY_HDG_PLACE_SORT_FORM = " order by hdg.placeSortForm ";
  private static final String HDG_NAME_SORT_FORM = ", hdg.nameSortForm ";

  /**
   * Gets the persistent class.
   *
   * @return the persistent class
   */
  @Override
  public Class getPersistentClass() {
    return PUBL_HDG.class;
  }

  /**
   * Gets the headings by sortform.
   *
   * @param operator        the operator
   * @param direction       the direction
   * @param term            the term
   * @param filter          the filter
   * @param cataloguingView the cataloguing view
   * @param count           the count
   * @param session         the session
   * @return the headings by sortform
   * @throws HibernateException the hibernate exception
   */
  @Override
  public List<Descriptor> getHeadingsBySortform(final String operator, final String direction, final String term, String filter, final int cataloguingView, final int count, final Session session)
    throws HibernateException {
      return getSortformBySearchTerm(operator, direction, term, filter, cataloguingView, count, session);
     }

  /**
   * Gets the sortform by  search term.
   *
   * @param operator        the operator
   * @param direction       the direction
   * @param term            the term
   * @param filter          the filter
   * @param cataloguingView the cataloguing view
   * @param count           the count
   * @param session         the session
   * @return the sortform by one search term
   * @throws HibernateException the hibernate exception
   */
  private List<Descriptor> getSortformBySearchTerm(final String operator, final String direction, final String term, final String filter, final int cataloguingView, final int count, final Session session)
    throws HibernateException {
    Query q =
      session.createQuery(
        "from "
          + getPersistentClass().getName()
          + " as hdg where hdg.placeSortForm "
          + operator
          + " :term  and "
          + " hdg.key.userViewString = '" + View.makeSingleViewString(cataloguingView) + "' "
          + filter
          + ORDER_BY_HDG_PLACE_SORT_FORM
          + direction
          + HDG_NAME_SORT_FORM
          + direction);
    q.setString("term", term);
    q.setMaxResults(count);
    List<Descriptor> publisherList = q.list();
    publisherList = (List<Descriptor>) isolateViewForList(publisherList, cataloguingView, session);
    return publisherList;
  }



  /**
   * Gets the browsing sort form.
   *
   * @param descriptor the descriptor
   * @return the browsing sort form
   */
  @Override
  public String getBrowsingSortForm(final Descriptor descriptor) {
    if (!(descriptor instanceof PUBL_HDG)) {
      throw new IllegalArgumentException();
    }
    final PUBL_HDG publisher = (PUBL_HDG) descriptor;
    return new StringBuilder()
      .append(publisher.getPlaceSortForm())
      .append(" : ").append(publisher.getNameSortForm()).toString();
  }


}
