
package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.authority.AuthorityHeaderFieldHelper;
import org.folio.marccat.business.cataloguing.common.GeographicAreaTag;

/**
 * @author elena
 *
 */
@SuppressWarnings("serial")
public class AuthorityGeographicAreaTag extends GeographicAreaTag {

	public AuthorityGeographicAreaTag() {
		super();
		setHeaderField(new AuthorityHeaderFieldHelper());
		setHeaderType((short) 3);
	}

}
