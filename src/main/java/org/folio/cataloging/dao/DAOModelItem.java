/*
 * (c) LibriCore
 * 
 * Created on Dec 20, 2005
 * 
 * DAOModelItem.java
 */
package org.folio.cataloging.dao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.folio.cataloging.business.cataloguing.common.ModelItem;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.ReferentialIntegrityException;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.common.TransactionalHibernateOperation;

/**
 * @author paulm
 * @since 1.0
 */
public abstract class DAOModelItem extends HibernateUtil {

	/**
	 * Delete a model item
	 *
	 * @param modelItem the model item
	 * @param session the hibernate session
	 * @throws DataAccessException in case of data access failure
	 */
	public void delete(final ModelItem modelItem, final Session session)
		throws ReferentialIntegrityException, DataAccessException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session session)
				throws HibernateException {
				session.delete(modelItem);
			}
		}
		.execute();
	}

	public ModelItem load(final int id, final Session session) throws DataAccessException {
		List<ModelItem> list = find(
				"from "
					+ getPersistentClass().getName()
					+ " as itm where itm.item = ? ",
				new Object[] { new Long(id)},
				new Type[] { Hibernate.LONG });
		final Optional<ModelItem> firstElement = list.stream().filter(Objects::nonNull).findFirst();
		return firstElement.isPresent() ? firstElement.get() : null;
		/*if (list.size() > 0) {
			modelItem = (ModelItem) list.get(0);
			return modelItem;
		} else {
			return null;
		}*/
	}

	protected abstract Class getPersistentClass();

	/**
		 * @return true if the given model is used by an item
		 * 
		 * @since 1.0
		 */
	public boolean getModelUsage(int modelIdNumber)
		throws DataAccessException {
		List list =
			find(
				"select count(*) from "
					+ getPersistentClass().getName()
					+ " as b"
					+ " where b.model.id = ?",
				new Object[] { new Integer(modelIdNumber)},
				new Type[] { Hibernate.INTEGER });
		return ((Integer) (list.get(0))).intValue() > 0;
	}
}
