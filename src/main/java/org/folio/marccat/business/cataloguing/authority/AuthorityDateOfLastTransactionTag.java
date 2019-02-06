package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.business.cataloguing.common.DateOfLastTransactionTag;


public class AuthorityDateOfLastTransactionTag
  extends DateOfLastTransactionTag {


  public AuthorityDateOfLastTransactionTag() {
    super();
    setHeaderField(new AuthorityHeaderFieldHelper());
    setHeaderType((short) 12);
  }

}
