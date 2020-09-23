package org.folio.marccat.dao.persistence;

import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.AuthorityModelDAO;

/**
 * @author elena
 *
 */
public class AuthorityModel extends Model {
	private static final AuthorityModelDAO THE_DAO = new AuthorityModelDAO();
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	/**
	 * Class constructor.
	 *
	 * @since 1.0
	 */
	public AuthorityModel() {
		super();
	}

	@Override
	public AbstractDAO getDAO() {
		return THE_DAO;
	}

}
