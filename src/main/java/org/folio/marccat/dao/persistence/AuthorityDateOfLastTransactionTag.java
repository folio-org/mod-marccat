
package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.authority.AuthorityHeaderFieldHelper;
import org.folio.marccat.business.cataloguing.common.DateOfLastTransactionTag;
import org.folio.marccat.business.common.PersistenceState;


/**
 * @author elena
 *
 */
public class AuthorityDateOfLastTransactionTag
  extends DateOfLastTransactionTag {


  public AuthorityDateOfLastTransactionTag() {
    super();
    setHeaderField(new AuthorityHeaderFieldHelper());
    setHeaderType((short) 12);
    setPersistenceState(new PersistenceState());
  }

}
