package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.RecordNotFoundException;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.persistence.FULL_CACHE;

import java.util.List;

/**
 * 2018 Paul Search Engine Java
 *
 * @author paulm
 * @since 1.0
 */
public class DAOFullCache extends HibernateUtil {
	
	private static final Log logger = LogFactory.getLog(DAOFullCache.class);

	public FULL_CACHE load(int itemNumber, int cataloguingView)
		throws RecordNotFoundException, DataAccessException {
		List l =
			find(
				"from FULL_CACHE as c "
					+ " where c.itemNumber = ? and c.userView = ?",
				new Object[] {
					new Integer(itemNumber),
					new Integer(cataloguingView)},
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
		if (l.size() == 0) {
			throw new RecordNotFoundException("Cache entry not found");
		}
		return (FULL_CACHE) l.get(0);
	}
		
}
