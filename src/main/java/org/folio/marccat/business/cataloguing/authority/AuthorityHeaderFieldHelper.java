package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.business.cataloguing.common.HeaderFieldHelper;
import org.folio.marccat.dao.persistence.T_AUT_HDR;


public class AuthorityHeaderFieldHelper extends HeaderFieldHelper {


  public int getCategory() {
    return 1;
  }


  public Class getHeaderListClass() {
    return T_AUT_HDR.class;
  }

}
