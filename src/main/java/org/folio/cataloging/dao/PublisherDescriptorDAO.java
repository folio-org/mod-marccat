package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.dao.persistence.Descriptor;
import org.folio.cataloging.dao.persistence.PUBL_HDG;

import java.sql.SQLException;
import java.util.List;

/**
 * Manages headings in the PUBL_HDG table.
 *
 * @author paulm
 * @author carment
 */
public class PublisherDescriptorDAO extends DAODescriptor {

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
	 * @param session the session
	 * @return the matching heading
	 * @throws HibernateException the hibernate exception
	 */
	@SuppressWarnings("unchecked")
	public PUBL_HDG getMatchingHeading(final Descriptor descriptor, final Session session)
			throws HibernateException {
		final PUBL_HDG publisher = (PUBL_HDG) descriptor;
		final List<PUBL_HDG> l = session.find("from "
						+ getPersistentClass().getName()
						+ " as c "
						+ " where c.nameStringText = ?"
						+ " and c.placeStringText = ? "
						+ " and c.indexingLanguage = ? "
						+ " and c.accessPointLanguage = ?"
						+ " and c.key.userViewString = ? ",
				new Object[] {
						publisher.getNameStringText(),
						publisher.getPlaceStringText(),
						publisher.getIndexingLanguage(),
						publisher.getAccessPointLanguage(),
						publisher.getUserViewString()},
				new Type[] {
						Hibernate.STRING,
						Hibernate.STRING,
						Hibernate.INTEGER,
						Hibernate.INTEGER,
						Hibernate.STRING });
		if (l.size() == 1) {
			return  l.get(0);
		} else {
			return null;
		}

	}

	/**
	 * Checks if is matching another heading.
	 *
	 * @param descriptor the descriptor
	 * @param session the session
	 * @return true, if is matching another heading
	 * @throws HibernateException the hibernate exception
	 */
	@SuppressWarnings("unchecked")
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
				new Object[] {
						publisher.getNameStringText(),
						publisher.getPlaceStringText(),
						publisher.getIndexingLanguage(),
						publisher.getAccessPointLanguage(),
						publisher.getUserViewString(),
						descriptor.getKey().getHeadingNumber()},
				new Type[] { Hibernate.STRING,
						Hibernate.STRING,
						Hibernate.INTEGER,
						Hibernate.INTEGER,
						Hibernate.STRING,
						Hibernate.INTEGER
				});
		return  publisherList.get(0) > 0;

	}


	/**
	 * Calculate search term.
	 *
	 * @param term the search term
	 * @param browseIndex the browse index
	 * @return the string
	 * @throws DataAccessException the data access exception
	 */
	public String calculateSearchTerm(final String term, final String browseIndex, final Session session)
			throws HibernateException, SQLException {
		String searchTerm = super.calculateSearchTerm(term, browseIndex, session);
		final  String[] parsedTerm = term.split(":");
		if (parsedTerm.length == 2){
			String place = parsedTerm[0].trim();
			String name = parsedTerm[1].trim();
			searchTerm = new StringBuilder().append(calculateSearchTerm(place, browseIndex, session))
					.append(" : ")
					.append(calculateSearchTerm(name, browseIndex, session)).toString();
		}
		return searchTerm;
	}

	/**
	 * Gets the document count.
	 *
	 * @param descriptor the descriptor
	 * @param searchingView the searching view
	 * @param session the session
	 * @return the document count
	 * @throws HibernateException the hibernate exception
	 */
	@SuppressWarnings("unchecked")
	public int getDocCount(final Descriptor descriptor, int searchingView, final Session session)
			throws HibernateException {
		if (searchingView == View.ANY) {
			List <Integer> countList =
					session.find(
							"select count(*) from PublisherAccessPoint as a, PUBL_TAG as b "
									+ " where a.headingNumber = b.publisherTagNumber "
									+ " and b.publisherHeadingNumber = ? ",
							new Object[]{
									descriptor.getHeadingNumber()},
							new Type[]{
									Hibernate.INTEGER});
			return countList.get(0);
		}
		else {
			List <Integer> countList =
					session.find(
							"select count(*) from PublisherAccessPoint as a, PUBL_TAG as b "
									+ " where a.headingNumber = b.publisherTagNumber "
									+ " and b.publisherHeadingNumber = ? "
									+ " and substr(ref.userViewString, ?, 1) = '1'",
							new Object[]{
									descriptor.getHeadingNumber(),
									searchingView},
							new Type[]{
									Hibernate.INTEGER,
									Hibernate.INTEGER});
			return countList.get(0);
		}

	}

}
