/*
 * (c) LibriCore
 *
 * Created on Nov 7, 2005
 *
 * TagImplFactory.java
 */
package org.folio.cataloging.business.cataloguing.common;

import org.folio.cataloging.business.cataloguing.authority.AuthorityTagImpl;
import org.folio.cataloging.business.cataloguing.bibliographic.BibliographicTagImpl;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class TagImplFactory {
  public static final int BIBLIOGRAPHIC = 1;
  public static final int AUTHORITY = 2;

  public static TagImpl getNewImplementation(int itemType) {
    if (itemType == AUTHORITY) {
      return new AuthorityTagImpl ( );
    } else {
      return new BibliographicTagImpl ( );
    }
  }

  public static TagImpl getDefaultImplementation() {
    return new BibliographicTagImpl ( );
  }
}
