package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.authority.AuthorityHeaderFieldHelper;
import org.folio.marccat.business.cataloguing.common.AuthenticationCodeTag;

/**
 * @author elena
 *
 */
@SuppressWarnings("serial")
public class AuthorityAuthenticationCodeTag extends AuthenticationCodeTag {

	public AuthorityAuthenticationCodeTag() {
		super();
		setHeaderField(new AuthorityHeaderFieldHelper());
		setHeaderType((short) 2);
	}

}
