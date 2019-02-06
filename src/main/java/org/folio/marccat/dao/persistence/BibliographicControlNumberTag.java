/*
 * (c) LibriCore
 *
 * Created on Nov 18, 2005
 *
 * BibliographicControlNumberTag.java
 */
package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.common.ControlNumberTag;


public class BibliographicControlNumberTag extends ControlNumberTag {


  public BibliographicControlNumberTag() {
    super();
    setHeaderType((short) 39);
  }

}
