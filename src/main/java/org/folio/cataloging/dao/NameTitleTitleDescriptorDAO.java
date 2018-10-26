package org.folio.cataloging.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import org.folio.cataloging.dao.persistence.Descriptor;
import org.folio.cataloging.dao.persistence.NME_TTL_HDG;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages headings NME_TTL_HDG for NTT index.
 *
 * @author paulm
 * @author carment
 */
public class NameTitleTitleDescriptorDAO extends NameTitleDescriptorDAO {

  /**
   * Gets the headings by sort form.
   *
   * @param operator        the operator
   * @param direction       the direction
   * @param term            the term
   * @param filter          the filter
   * @param cataloguingView the cataloguing view
   * @param count           the count
   * @param session         the session
   * @return the headings by sort form
   * @throws HibernateException the hibernate exception
   */
  public List <Descriptor> getHeadingsBySortform(final String operator, final String direction, final String term, final String filter, final int cataloguingView, final int count, final Session session)
    throws HibernateException {
    final Query q = session.createQuery(
      "select distinct hdg, nme.sortForm, ttl.sortForm from "
        + "NME_TTL_HDG as hdg, "
        + "NME_HDG as nme, "
        + "TTL_HDG as ttl"
        + " where hdg.nameHeadingNumber = nme.key.headingNumber "
        + " and hdg.titleHeadingNumber = ttl.key.headingNumber "
        + " and ttl.sortForm "
        + operator
        + " :term  and "
        + " SUBSTR(hdg.key.userViewString, :view, 1) = '1' "
        + filter
        + " order by ttl.sortForm "
        + direction
        + ", nme.sortForm "
        + direction);
    q.setString("term", term);
    q.setInteger("view", cataloguingView);
    q.setMaxResults(count);
    final List <?> nameTitleHedingList = q.list();
    final List <NME_TTL_HDG> nameTitleHedings = new ArrayList();
    nameTitleHedingList.forEach(nameTitleHeading -> nameTitleHedings.add((NME_TTL_HDG) ((Object[]) nameTitleHeading)[0]));
    final List isolateHeadingList = isolateViewForList(nameTitleHedings, cataloguingView, session);
    loadHeadings(isolateHeadingList, cataloguingView, session);
    return isolateHeadingList;

  }

  /**
   * Gets the browsing sort form.
   *
   * @param descriptor the heading(NME_TTL_HDG)
   * @return the browsing sort form
   */
  public String getBrowsingSortForm(final Descriptor descriptor) {
    if (!(descriptor instanceof NME_TTL_HDG)) {
      throw new IllegalArgumentException();
    }
    return ((NME_TTL_HDG) descriptor).getTitleHeading().getSortForm();
  }

}
