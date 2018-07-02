package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.business.common.RecordNotFoundException;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.persistence.FULL_CACHE;
import org.folio.cataloging.log.Log;

import java.util.List;

/**
 * 2018 Paul Search Engine Java
 *
 * @author paulm
 * @since 1.0
 */
public class DAOFullCache extends HibernateUtil {
	
	private static final Log logger = new Log(DAOFullCache.class);

	public FULL_CACHE load(final Session session, int itemNumber, int cataloguingView)
		throws RecordNotFoundException {
		List l =
			find(
				"from FULL_CACHE as c "
					+ " where c.itemNumber = ? and c.userView = ?",
				new Object[] { itemNumber, cataloguingView},
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
		if (l.size() == 0) {
			throw new RecordNotFoundException("Cache entry not found");
		}
		return (FULL_CACHE) l.get(0);
	}
		
}
