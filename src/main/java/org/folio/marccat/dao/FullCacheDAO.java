package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.dao.persistence.FULL_CACHE;
import org.folio.marccat.exception.RecordNotFoundException;

import java.util.List;

/**
 * Search Engine Java
 *
 * @author paulm
 * @author cchiama
 * @since 1.0
 */
public class FullCacheDAO extends AbstractDAO {

  @SuppressWarnings("unchecked")
  public FULL_CACHE load(final Session session, final int itemNumber, final int cataloguingView) {
    final List<FULL_CACHE> list =
      find(session,
        "from FULL_CACHE as c "
          + " where c.itemNumber = ? and c.userView = ?",
        new Object[]{itemNumber, cataloguingView},
        new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});
    if (list.isEmpty()) {
      throw new RecordNotFoundException("Cache entry not found");
    }
    return list.get(0);
  }

}
