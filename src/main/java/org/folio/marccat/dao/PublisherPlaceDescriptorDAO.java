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

    final String[] parsedTerm = term.split(" : ");
    if (parsedTerm.length < 2) {
      return getSortformByOneSearchTerm(operator, direction, term, filter, cataloguingView, count, session);
    } else {
      return getSortformByTwoSearchTerms(operator, direction, filter, cataloguingView, count, parsedTerm, session);
    }
  }

  /**
   * Gets the sortform by one search term.
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
  private List<Descriptor> getSortformByOneSearchTerm(final String operator, final String direction, final String term, final String filter, final int cataloguingView, final int count, final Session session)
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
          + " order by hdg.placeSortForm "
          + direction
          + ", hdg.nameSortForm "
          + direction);
    q.setString("term", term);
    q.setMaxResults(count);
    List<Descriptor> publisherList = q.list();
    publisherList = (List<Descriptor>) isolateViewForList(publisherList, cataloguingView, session);
    return publisherList;
  }


  /**
   * Gets the sortform by two search terms.
   *
   * @param operator        the operator
   * @param direction       the direction
   * @param filter          the filter
   * @param cataloguingView the cataloguing view
   * @param count           the count
   * @param parsedTerm      the parsed term
   * @param session         the session
   * @return the sortform by two search terms
   * @throws HibernateException the hibernate exception
   */
  private List<Descriptor> getSortformByTwoSearchTerms(final String operator, final String direction, final String filter, final int cataloguingView, final int count, final String[] parsedTerm, final Session session)
    throws HibernateException {
    final String name;
    final String place;
    place = parsedTerm[0].trim();
    name = parsedTerm[1].trim();
    List<Descriptor> publisherList = null;
    String viewClause = "";

    if (cataloguingView != View.ANY) {
      viewClause = " and SUBSTR(hdg.key.userViewString, " + cataloguingView + ", 1) = '1' ";
    }
    if (operator.equals("<")) {
      final Query q =
        session.createQuery(
          "from "
            + getPersistentClass().getName()
            + " as hdg where hdg.nameSortForm "
            + operator
            + " :name  and "
            + " hdg.placeSortForm "
            + (operator.equals("<") ? "<=" : operator)
            + " :place "
            + viewClause
            + filter
            + " order by hdg.placeSortForm "
            + direction
            + ", hdg.nameSortForm "
            + direction);
      q.setString("place", place);
      q.setString("name", name);
      q.setMaxResults(count);
      publisherList = q.list();
      publisherList = (List<Descriptor>) isolateViewForList(publisherList, cataloguingView, session);
      return publisherList;

    } else if (operator.contains(">=") || operator.contains("<=")) {
      String nextOperator = operator;
      nextOperator = nextOperator.replace("=", "");

      final String select = "select distinct hdg from "
        + getPersistentClass().getName()
        + " as hdg where "
        + " (hdg.placeSortForm = "
        + " :place  and "
        + " hdg.nameSortForm "
        + operator
        + " :name)"
        + " or "
        + "hdg.placeSortForm "
        + nextOperator
        + " :place ";

      final Query firstQuery =
        session.createQuery(select
          + viewClause
          + filter
          + " order by hdg.placeSortForm "
          + direction
          + ", hdg.nameSortForm "
          + direction);
      firstQuery.setString("place", place);
      firstQuery.setString("name", name);
      firstQuery.setMaxResults(count);
      publisherList = firstQuery.list();
      return publisherList;
    }
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
