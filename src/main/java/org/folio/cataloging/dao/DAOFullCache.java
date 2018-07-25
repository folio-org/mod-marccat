package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.business.common.RecordNotFoundException;
import org.folio.cataloging.dao.persistence.FULL_CACHE;

import java.util.List;

/**
 * 2018 Paul Search Engine Java
 *
 * @author paulm
 * @author agazzarini
 * @since 1.0
 */
public class DAOFullCache extends AbstractDAO {

	@SuppressWarnings("unchecked")
	public FULL_CACHE load(final Session session, final int itemNumber, final int cataloguingView) throws RecordNotFoundException {
		final List<FULL_CACHE> list =
			find(session,
				"from FULL_CACHE as c "
					+ " where c.itemNumber = ? and c.userView = ?",
				new Object[] { itemNumber, cataloguingView},
				new Type[] { Hibernate.INTEGER, Hibernate.INTEGER });
		if (list.size() == 0) {
			throw new RecordNotFoundException("Cache entry not found");
		}
		return list.get(0);
	}
		
}
