package org.folio.marccat.business.cataloguing.common;

import org.folio.marccat.business.cataloguing.bibliographic.BibliographicTagImpl;


public class TagImplFactory {

  private TagImplFactory() {
  }

  public static TagImpl getDefaultImplementation() {
    return new BibliographicTagImpl();
  }
}
