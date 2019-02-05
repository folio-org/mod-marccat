package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.business.cataloguing.common.AuthenticationCodeTag;


public class AuthorityAuthenticationCodeTag extends AuthenticationCodeTag {

  public AuthorityAuthenticationCodeTag() {
    super();
    setHeaderField(new AuthorityHeaderFieldHelper());
    setHeaderType(2);
  }

}
