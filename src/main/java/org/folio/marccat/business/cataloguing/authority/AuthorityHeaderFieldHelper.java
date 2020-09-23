package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.business.cataloguing.common.HeaderFieldHelper;
import org.folio.marccat.dao.persistence.T_AUT_HDR;

/**
 * @author elena
 *
 */
public class AuthorityHeaderFieldHelper extends HeaderFieldHelper {

	  public int getCategory() {
	    return 1;
	  }

	  public Class<T_AUT_HDR> getHeaderListClass() {
	    return T_AUT_HDR.class;
	  }

}
