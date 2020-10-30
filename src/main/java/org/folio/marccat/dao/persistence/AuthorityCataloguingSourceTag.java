package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.authority.AuthorityHeaderFieldHelper;
import org.folio.marccat.business.cataloguing.common.CataloguingSourceTag;
import org.folio.marccat.business.common.PersistenceState;

/**
 * @author elena
 *
 */
public class AuthorityCataloguingSourceTag extends CataloguingSourceTag {

  public AuthorityCataloguingSourceTag() {
    super();
    setHeaderField(new AuthorityHeaderFieldHelper());
    setHeaderType((short) 1);
    setPersistenceState(new PersistenceState());
  }

}
