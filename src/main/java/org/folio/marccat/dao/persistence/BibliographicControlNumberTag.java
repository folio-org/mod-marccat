package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.common.ControlNumberTag;


public class BibliographicControlNumberTag extends ControlNumberTag {


  public BibliographicControlNumberTag() {
    super();
    setHeaderType((short) 39);
  }

}
