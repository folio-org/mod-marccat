
package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.authority.AuthorityHeaderFieldHelper;
import org.folio.marccat.business.cataloguing.common.ControlNumberTag;


/**
 * @author elena
 *
 */
public class AuthorityControlNumberTag extends ControlNumberTag {


  public AuthorityControlNumberTag() {
    super();
    setHeaderField(new AuthorityHeaderFieldHelper());
    setHeaderType((short) 11);
  }

}
