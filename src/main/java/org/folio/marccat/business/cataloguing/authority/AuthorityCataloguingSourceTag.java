package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.business.cataloguing.common.CataloguingSourceTag;

public class AuthorityCataloguingSourceTag extends CataloguingSourceTag {


  public AuthorityCataloguingSourceTag() {
    super();
    setHeaderField(new AuthorityHeaderFieldHelper());
    setHeaderType((short) 1);
  }

}
