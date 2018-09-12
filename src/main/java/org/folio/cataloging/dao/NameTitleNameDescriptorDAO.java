package org.folio.cataloging.dao;


import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.dao.persistence.Descriptor;
import org.folio.cataloging.dao.persistence.NME_TTL_HDG;

import java.util.List;

/**
 * Manages headings in the NME_TTL_HDG table for NTN index.
 *
 * @author paulm
 * @author carment
 */
public class NameTitleNameDescriptorDAO extends NameTitleDescriptorDAO {

	/**
	 * Gets the headings by sort form.
	 *
	 * @param operator the operator
	 * @param direction the direction
	 * @param term the term
	 * @param filter the filter
	 * @param cataloguingView the cataloguing view
	 * @param count the count
	 * @param session the session
	 * @return the headings by sort form
	 * @throws HibernateException the hibernate exception
	 */
	@SuppressWarnings("unchecked")
	public List getHeadingsBySortform(final String operator, final String direction, final String term, final String filter, final int cataloguingView, final int count, final Session session)
			throws HibernateException
	{
		String[] parsedTerm = term.split(" : ");
		if (parsedTerm.length < 2) {
			return getSortformByOneSearchTerm(operator, direction, term, filter, cataloguingView, count, session);
		}
		else{
			return getSortformByTwoSearchTerms(operator, direction, filter, cataloguingView, count, session, parsedTerm);
		}
	}

	/**
	 * Gets the sort form of the heading(NME_TTL_HDG) by one search term.
	 *
	 * @param operator the operator
	 * @param direction the direction
	 * @param name the name
	 * @param filter the filter
	 * @param searchingView the searching view
	 * @param count the count
	 * @param session the session
	 * @return the sort form by one search term
	 * @throws HibernateException the hibernate exception
	 */
	private List<NME_TTL_HDG> getSortformByOneSearchTerm(final String operator, final String direction, final String name, final String filter, final int searchingView, final int count, final Session session)
			throws HibernateException{
		final Query q = session.createQuery(
				"select distinct hdg from "
						+ "NME_TTL_HDG as hdg, "
						+ "NME_HDG as nme, "
						+ "TTL_HDG as ttl"
						+ " where hdg.nameHeadingNumber = nme.key.headingNumber "
						+ " and hdg.titleHeadingNumber = ttl.key.headingNumber "
						+ " and nme.sortForm " + operator + " :name "
						+ " and SUBSTR(hdg.key.userViewString, :view, 1) = '1' " + filter
						+ " order by nme.sortForm " + direction + ", ttl.sortForm " + direction);
		q.setString("name", name);
		q.setInteger("view", searchingView);
		q.setMaxResults(count);
		final List<NME_TTL_HDG> nameTitleHedingList = q.list();
		final List isolateHeadingList = isolateViewForList(nameTitleHedingList, searchingView, session);
		loadHeadings((NME_TTL_HDG) isolateHeadingList, searchingView, session);
		return isolateHeadingList;
	}

	/**
	 * Gets the sort form of the heading(NME_TTL_HDG) by two search terms.
	 *
	 * @param operator the operator
	 * @param direction the direction
	 * @param filter the filter
	 * @param searchingView the searching view
	 * @param count the count
	 * @param session the session
	 * @param parsedTerm the parsed term
	 * @return the sort form by two search terms
	 * @throws HibernateException the hibernate exception
	 */
	private List<NME_TTL_HDG> getSortformByTwoSearchTerms(final String operator, final String direction, final String filter, final int searchingView, final int count, final Session session, final String[] parsedTerm)
			throws HibernateException {
		String name;
		String title;
		String nextOperator;
		String viewClause = "";
		name = parsedTerm[0].trim();
		title = parsedTerm[1].trim();
		List <NME_TTL_HDG> isolateHeadingList = null;
		if (searchingView != View.ANY ) {
			viewClause = " and SUBSTR(hdg.key.userViewString, " + searchingView + ", 1) = '1' ";
		}
		if (operator.equals("<")) {
			Query q = session.createQuery(
					"Select distinct hdg from "
							+ "NME_TTL_HDG as hdg, "
							+ "NME_HDG as nme, "
							+ "TTL_HDG as ttl"
							+ " where hdg.nameHeadingNumber = nme.key.headingNumber "
							+ " and hdg.titleHeadingNumber = ttl.key.headingNumber "
							+ " and (nme.sortForm " + operator + " :name "
							+ " or (nme.sortForm = :name "
							+ " and ttl.sortForm " + operator + " :title)) "
							+ viewClause
							+ filter
							+ " order by nme.sortForm " + direction + ", ttl.sortForm " + direction);
			q.setString("name", name);
			q.setString("title", title);
			q.setMaxResults(count);
			final List <NME_TTL_HDG> nameTitleHeadingList = q.list();
			isolateHeadingList = isolateViewForList(nameTitleHeadingList, searchingView);
			loadHeadings((NME_TTL_HDG) isolateHeadingList, searchingView, session);
			return isolateHeadingList;

		} else if (operator.contains(">=") || operator.contains("<=")) {
			nextOperator = operator;
			nextOperator = nextOperator.replaceAll("=","");
			final Query firstQuery = session.createQuery(
					"Select distinct hdg from "
							+ "NME_TTL_HDG as hdg, "
							+ "NME_HDG as nme, "
							+ "TTL_HDG as ttl"
							+ " where hdg.nameHeadingNumber = nme.key.headingNumber "
							+ " and hdg.titleHeadingNumber = ttl.key.headingNumber "
							+ " and nme.sortForm = :name "
							+ " and ttl.sortForm " + operator + " :title "
							+ viewClause
							+ filter
							+ " order by nme.sortForm " + direction + ", ttl.sortForm " + direction);
			firstQuery.setString("name", name);
			firstQuery.setString("title", title);
			firstQuery.setMaxResults(count);
			final List <NME_TTL_HDG> nameTitleHeadingList = firstQuery.list();
			isolateHeadingList = isolateViewForList(nameTitleHeadingList, searchingView);
			loadHeadings((NME_TTL_HDG) isolateHeadingList, searchingView, session);

			final Query secondQuery = session.createQuery(
					"Select distinct hdg from "
							+ "NME_TTL_HDG as hdg, "
							+ "NME_HDG as nme, "
							+ "TTL_HDG as ttl"
							+ " where hdg.nameHeadingNumber = nme.key.headingNumber "
							+ " and hdg.titleHeadingNumber = ttl.key.headingNumber "
							+ " and nme.sortForm " + nextOperator + " :name "
							+ viewClause
							+ filter
							+ " order by nme.sortForm " + direction + ", ttl.sortForm " + direction);
			secondQuery.setString("name", name);
			//secondQuery.setString("title", title);
			secondQuery.setMaxResults(count);
			List <NME_TTL_HDG> secondNameTitleHeadingList = secondQuery.list();
			final List <NME_TTL_HDG> secondIsolateHeadingList = isolateViewForList(secondNameTitleHeadingList, searchingView);
			loadHeadings((NME_TTL_HDG) secondIsolateHeadingList, searchingView, session);
			isolateHeadingList.addAll(secondIsolateHeadingList);
			return isolateHeadingList;
		}
		return isolateHeadingList;
	}

	/**
	 * Gets the browsing sort form.
	 *
	 * @param descriptor the heading(NME_TTL_HDG)
	 * @return the browsing sort form
	 */
	public String getBrowsingSortForm(final Descriptor descriptor)
	{
		if (!(descriptor instanceof NME_TTL_HDG)) {
			throw new IllegalArgumentException();
		}
		return new StringBuilder().append(((NME_TTL_HDG) descriptor).getNameHeading().getSortForm())
				.append(" : ")
				.append(((NME_TTL_HDG) descriptor).getTitleHeading().getSortForm()).toString();
	}
}
