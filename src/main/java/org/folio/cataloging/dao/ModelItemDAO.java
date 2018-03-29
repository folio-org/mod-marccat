package org.folio.cataloging.dao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.folio.cataloging.dao.persistence.ModelItem;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.ReferentialIntegrityException;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;


/**
 * Abstract class for common implementations of ModelItemsDAO (Bib and Auth).
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public abstract class ModelItemDAO extends HibernateUtil {

	/**
	 * Delete a model item.
	 *
	 * @param modelItem the model item
	 * @param session the hibernate session
	 * @throws ReferentialIntegrityException the referential integrity exception
	 * @throws HibernateException in case of data access failure
	 */
	public void delete(final ModelItem modelItem, final Session session)
		throws ReferentialIntegrityException, HibernateException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session session)
				throws HibernateException {
				session.delete(modelItem);
			}
		}
		.execute();
	}

	/**
	 * Return a model item by id.
	 *
	 * @param id the id of the model item
	 * @param session the hibernate session
	 * @return a model item.
	 * @throws DataAccessException in case of data access failure
	 */
	public ModelItem load(final int id, final Session session) throws HibernateException {
		List<ModelItem> list = session.find(
				"from "
					+ getPersistentClass().getName()
					+ " as itm where itm.item = ? ",
				new Object[] { new Long(id)},
				new Type[] { Hibernate.LONG });
		final Optional<ModelItem> firstElement = list.stream().filter(Objects::nonNull).findFirst();
		return firstElement.isPresent() ? firstElement.get() : null;
	}

	protected abstract Class getPersistentClass();

	/**
	 * Return true if the given model is used by an item.
	 *
	 * @param id the id of the model item
	 * @return true if the given model is used by an item
	 * @throws HibernateException in case of data access failure
	 */
	public boolean getModelUsage(final int id, final Session session ) throws HibernateException {
		List <Integer> list =
				session.find(
				"select count(*) from "
					+ getPersistentClass().getName()
					+ " as b"
					+ " where b.model.id = ?",
				new Object[] { new Integer(id)},
				new Type[] { Hibernate.INTEGER });

		final Optional<Integer> firstElement = list.stream().filter(Objects::nonNull).findFirst().filter(count -> count > 0);
		return firstElement.isPresent();
	}
}
