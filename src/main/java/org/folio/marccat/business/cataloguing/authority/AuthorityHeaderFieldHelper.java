/*
 * (c) LibriCore
 *
 * Created on Nov 18, 2005
 *
 * AuthorityHeaderFieldHelper.java
 */
package org.folio.marccat.business.cataloguing.authority;

import org.folio.marccat.business.cataloguing.common.HeaderFieldHelper;
import org.folio.marccat.dao.persistence.T_AUT_HDR;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class AuthorityHeaderFieldHelper extends HeaderFieldHelper {

  /* (non-Javadoc)
   * @see HeaderField#getCategory()
   */
  public int getCategory() {
    return 1;
  }

  /* (non-Javadoc)
   * @see HeaderFieldHelper#getHeaderListClass()
   */
  public Class getHeaderListClass() {
    return T_AUT_HDR.class;
  }

}
