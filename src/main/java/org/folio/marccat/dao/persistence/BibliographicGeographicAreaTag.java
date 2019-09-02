package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.common.GeographicAreaTag;


public class BibliographicGeographicAreaTag extends GeographicAreaTag {


  public BibliographicGeographicAreaTag() {
    super();
    setHeaderType((short) 5);
  }

}
