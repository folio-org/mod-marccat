package org.folio.cataloging.dao;

import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;

/**
 * @author paulm
 * @since 1.0
 */
public class DAOBibliographicNote extends AbstractDAO {

  public void delete(Persistence p) throws DataAccessException {
    super.delete (p);
  }

}
