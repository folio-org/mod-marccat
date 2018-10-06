package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.business.cataloguing.common.CataloguingSourceTag;
import org.folio.cataloging.business.common.PersistenceState;

/**
 * @author paulm
 * @since 1.0
 */
public class BibliographicCataloguingSourceTag extends CataloguingSourceTag {

  /**
   * Class constructor
   *
   * @since 1.0
   */
  public BibliographicCataloguingSourceTag() {
    super ( );
    setHeaderType ((short) 1);
    setPersistenceState (new PersistenceState ( ));
  }

}
