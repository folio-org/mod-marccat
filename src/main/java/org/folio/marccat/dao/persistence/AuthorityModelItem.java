package org.folio.marccat.dao.persistence;

import java.io.Serializable;

import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.dao.AuthorityModelItemDAO;

/**
 * @author elena
 *
 */
public class AuthorityModelItem extends ModelItem implements Persistence, Serializable {

	  /**
	   * Gets the dao.
	   *
	   * @return the dao
	   */
	  public AuthorityModelItemDAO getDAO() {
	    return new AuthorityModelItemDAO();
	  }

}
