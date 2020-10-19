package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.authority.AuthorityHeaderFieldHelper;
import org.folio.marccat.business.cataloguing.common.AuthenticationCodeTag;

/**
 * @author elena
 *
 */
public class AuthorityAuthenticationCodeTag extends AuthenticationCodeTag {

  /**
   * 
   */
  private static final long serialVersionUID = 4233818442270977878L;

  public AuthorityAuthenticationCodeTag() {
    super();
    setHeaderField(new AuthorityHeaderFieldHelper());
    setHeaderType((short) 2);
  }

}
