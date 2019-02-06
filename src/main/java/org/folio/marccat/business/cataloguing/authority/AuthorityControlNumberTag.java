package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.business.cataloguing.common.ControlNumberTag;


public class AuthorityControlNumberTag extends ControlNumberTag {


  public AuthorityControlNumberTag() {
    super();
    setHeaderField(new AuthorityHeaderFieldHelper());
    setHeaderType((short) 11);
  }
}
