package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.common.AuthenticationCodeTag;


public class BibliographicAuthenticationCodeTag extends AuthenticationCodeTag {


  public BibliographicAuthenticationCodeTag() {
    super();
    setHeaderType((short) 4);
  }

}
