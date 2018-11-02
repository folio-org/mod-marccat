/*
 * (c) LibriCore
 *
 * Created on Nov 18, 2005
 *
 * AuthorityDateOfLastTransactionTag.java
 */
package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.business.cataloguing.common.DateOfLastTransactionTag;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class AuthorityDateOfLastTransactionTag
  extends DateOfLastTransactionTag {

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public AuthorityDateOfLastTransactionTag() {
    super();
    setHeaderField(new AuthorityHeaderFieldHelper());
    setHeaderType((short) 12);
  }

}
