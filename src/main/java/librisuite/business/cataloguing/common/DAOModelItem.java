/*
 * (c) LibriCore
 * 
 * Created on Dec 20, 2005
 * 
 * DAOModelItem.java
 */
package librisuite.business.cataloguing.common;

import java.util.List;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.ReferentialIntegrityException;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import com.libricore.librisuite.common.HibernateUtil;
import com.libricore.librisuite.common.TransactionalHibernateOperation;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/12/21 08:30:33 $
 * @since 1.0
 */
public abstract class DAOModelItem extends HibernateUtil {

	public void delete(final ModelItem modelItem)
		throws ReferentialIntegrityException, DataAccessException {
		new TransactionalHibernateOperation() {
			public void doInHibernateTransaction(Session session)
				throws HibernateException {
				session.delete(modelItem);
			}
		}
		.execute();
	}

	public ModelItem load(int id) throws DataAccessException {
		ModelItem modelItem = null;
		List list =
			find(
				"from "
					+ getPersistentClass().getName()
					+ " as itm where itm.item = ? ",
				new Object[] { new Long(id)},
				new Type[] { Hibernate.LONG });
		if (list.size() > 0) {
			modelItem = (ModelItem) list.get(0);
			return modelItem;
		} else {
			return null;
		}
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
