package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.business.cataloguing.common.GeographicAreaTag;

public class AuthorityGeographicAreaTag extends GeographicAreaTag {


  public AuthorityGeographicAreaTag() {
    super();
    setHeaderField(new AuthorityHeaderFieldHelper());
    setHeaderType((short) 3);
  }

}
