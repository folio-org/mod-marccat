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
 * special to publishers when Publisher name is being browsed.
 *
 * @author paulm
 * @author carment
 */
public class PublisherNameDescriptorDAO extends PublisherDescriptorDAO {

  private static final String ORDER_BY_HDG_NAME_SORT_FORM = " order by hdg.nameSortForm ";
  private static final String HDG_PLACE_SORT_FORM = ", hdg.placeSortForm ";

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
   * @param operator      the operator
   * @param direction     the direction
   * @param term          the term
   * @param filter        the filter
   * @param searchingView the searching view
   * @param count         the count
   * @param session       the session
   * @return the headings by sortform
   * @throws HibernateException the hibernate exception
   */
  @Override
  public List<Descriptor> getHeadingsBySortform(final String operator, final String direction, final String term, final String filter, final int searchingView, final int count, final Session session)
    throws HibernateException {
    final String[] parsedTerm = term.split(" : ");
    if (parsedTerm.length < 2) {
      return getSortformByOneSearchTerm(operator, direction, term, filter, searchingView, count, session);
    } else {
      return getSortformByTwoSearchTerms(operator, direction, filter, searchingView, count, parsedTerm, session);
    }
  }

  /**
   * Gets the sortform by two search terms.
   *
   * @param operator      the operator
   * @param direction     the direction
   * @param filter        the filter
   * @param searchingView the searching view
   * @param count         the count
   * @param parsedTerm    the parsed term
   * @param session       the session
   * @return the sortform by two search terms
   * @throws HibernateException the hibernate exception
   */
  private List<Descriptor> getSortformByTwoSearchTerms(final String operator, final String direction, final String filter, final int searchingView, final int count, final String[] parsedTerm, final Session session)
    throws HibernateException {
    final String name;
    final String place;
    String viewClause = "";
    place = parsedTerm[0].trim();
    name = parsedTerm[1].trim();
    List<Descriptor> publisherList = null;

    if (searchingView != View.ANY) {
      viewClause = " and hdg.key.userViewString = '" + View.makeSingleViewString(searchingView) + "' ";
    }

    if (operator.equals("<")) {
      Query q =
        session.createQuery(
          "from "
            + getPersistentClass().getName()
            + " as hdg where hdg.nameSortForm "
            + (operator.equals("<") ? "<=" : operator)
            + " :name  and "
            + " hdg.placeSortForm "
            + operator
            + " :place "
            + viewClause
            + filter
            + ORDER_BY_HDG_NAME_SORT_FORM
            + direction
            + HDG_PLACE_SORT_FORM
            + direction);
      q.setString("place", place);
      q.setString("name", name);
      q.setMaxResults(count);
      publisherList = q.list();
      return publisherList;

    } else if (operator.contains(">=") || operator.contains("<=")) {
      String nextOperator = operator;
      nextOperator = nextOperator.replace("=", "");

      final String select = "select distinct hdg from "
        + getPersistentClass().getName()
        + " as hdg where "
        + " (hdg.nameSortForm = "
        + " :name  and "
        + " hdg.placeSortForm "
        + operator
        + " :place)"
        + " or "
        + "hdg.nameSortForm "
        + nextOperator
        + " :name ";

      final Query q =
        session.createQuery(select
          + viewClause
          + filter
          + ORDER_BY_HDG_NAME_SORT_FORM
          + direction
          + ", hdg.placeSortForm "
          + direction);

      q.setString("place", place);
      q.setString("name", name);
      q.setMaxResults(count);
      publisherList = q.list();
      return publisherList;

    }
    return publisherList;
  }

  /**
   * Gets the sortform by one search term.
   *
   * @param operator      the operator
   * @param direction     the direction
   * @param term          the term
   * @param filter        the filter
   * @param searchingView the searching view
   * @param count         the count
   * @param session       the session
   * @return the sortform by one search term
   * @throws HibernateException the hibernate exception
   */
  private List<Descriptor> getSortformByOneSearchTerm(final String operator, final String direction, final String term, final String filter, final int searchingView, final int count, final Session session)
    throws HibernateException {
    String viewClause = "";
    if (searchingView != View.ANY) {
      viewClause = " and hdg.key.userViewString = '" + View.makeSingleViewString(searchingView) + "' ";
    }
    final Query q =
      session.createQuery(
        "from "
          + getPersistentClass().getName()
          + " as hdg where hdg.nameSortForm "
          + operator
          + " :term  "
          + viewClause
          + filter
          + ORDER_BY_HDG_NAME_SORT_FORM
          + direction
          + ", hdg.placeSortForm "
          + direction);
    q.setString("term", term);
    q.setMaxResults(count);
    return q.list();
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
    PUBL_HDG publisher = (PUBL_HDG) descriptor;
    return new StringBuilder().append(publisher.getPlaceSortForm())
      .append(" : ")
      .append(publisher.getNameSortForm()).toString();
  }
}
