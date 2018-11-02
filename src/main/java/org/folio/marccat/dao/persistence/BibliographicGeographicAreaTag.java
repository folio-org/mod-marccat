/*
 * (c) LibriCore
 *
 * Created on Nov 18, 2005
 *
 * BibliographicGeographicAreaTag.java
 */
package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.common.GeographicAreaTag;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class BibliographicGeographicAreaTag extends GeographicAreaTag {

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public BibliographicGeographicAreaTag() {
    super();
    setHeaderType((short) 5);
  }

}
