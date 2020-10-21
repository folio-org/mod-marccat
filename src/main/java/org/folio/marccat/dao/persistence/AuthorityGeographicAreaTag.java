package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.authority.AuthorityHeaderFieldHelper;
import org.folio.marccat.business.cataloguing.common.GeographicAreaTag;

/**
 * @author elena
 *
 */
public class AuthorityGeographicAreaTag extends GeographicAreaTag {

	private static final long serialVersionUID = 2361636226646320903L;

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public AuthorityGeographicAreaTag() {
		super();
		setHeaderField(new AuthorityHeaderFieldHelper());
		setHeaderType((short) 3);
	}

}
