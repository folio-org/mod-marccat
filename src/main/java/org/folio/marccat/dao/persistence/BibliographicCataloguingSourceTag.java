package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.common.CataloguingSourceTag;
import org.folio.marccat.business.common.PersistenceState;

/**


 */
public class BibliographicCataloguingSourceTag extends CataloguingSourceTag {


  public BibliographicCataloguingSourceTag() {
    super();
    setHeaderType((short) 1);
    setPersistenceState(new PersistenceState());
  }

}
