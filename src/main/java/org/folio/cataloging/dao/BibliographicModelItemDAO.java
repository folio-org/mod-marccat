package org.folio.cataloging.dao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.business.cataloguing.bibliographic.BibliographicModelItem;


/**
 * @author Wim Crols
 * @since 1.0
 */
public class BibliographicModelItemDAO extends ModelItemDAO {

	/* (non-Javadoc)
	 * @see DAOModelItem#getPersistentClass()
	 */
	protected Class getPersistentClass() {
		return BibliographicModelItem.class;
	}

	/**
	 * Return true if the given model is used by an item
	 *
	 * @param bibItem the id of the model item
	 * @return true if the given model is used by an item
	 * @throws HibernateException in case of data access failure
	 */
public boolean getModelUsageByItem(int bibItem, final Session session)
	throws HibernateException {
	List <Integer> list =
		find(
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
