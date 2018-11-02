package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.business.cataloguing.common.AuthenticationCodeTag;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class AuthorityAuthenticationCodeTag extends AuthenticationCodeTag {

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public AuthorityAuthenticationCodeTag() {
    super();
    setHeaderField(new AuthorityHeaderFieldHelper());
    setHeaderType(2);
  }

}
