package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.dao.persistence.BibliographicModelItem;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The Class BibliographicModelItemDAO.
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public class BibliographicModelItemDAO extends ModelItemDAO {

	/**
	 * Gets the persistent class.
	 *
	 * @return the persistent class
	 */
	protected Class getPersistentClass() {
		return BibliographicModelItem.class;
	}

	/**
	 * Return true if the given model is used by an item
	 *
	 * @param bibItem the id of the model item
	 * @param session the session
	 * @return true if the given model is used by an item
	 * @throws HibernateException in case of data access failure
	 */
public boolean getModelUsageByItem(int bibItem, final Session session)
	throws HibernateException {
	List<Integer> list =
			session.find(
			"select count(*) from "
				+ getPersistentClass().getName()
				+ " as b"
				+ " where b.item = ?",
			new Object[] { new Integer(bibItem)},
			new Type[] { Hibernate.INTEGER });
	final Optional<Integer> firstElement = list.stream().filter(Objects::nonNull).findFirst().filter(count -> count > 0);
	return firstElement.isPresent();
}

}
