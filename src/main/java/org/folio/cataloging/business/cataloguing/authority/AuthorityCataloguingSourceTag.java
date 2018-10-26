/*
 * (c) LibriCore
 *
 * Created on Nov 18, 2005
 *
 * AuthorityCataloguingSourceTag.java
 */
package org.folio.cataloging.business.cataloguing.authority;

import org.folio.cataloging.business.cataloguing.common.CataloguingSourceTag;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class AuthorityCataloguingSourceTag extends CataloguingSourceTag {

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public AuthorityCataloguingSourceTag() {
    super();
    setHeaderField(new AuthorityHeaderFieldHelper());
    setHeaderType((short) 1);
  }

}
