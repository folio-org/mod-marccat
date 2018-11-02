package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.dao.BibliographicModelItemDAO;

import java.io.Serializable;

/**
 * Represents a Model/Template for initiating new bibliographic item
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public class BibliographicModelItem extends ModelItem implements Persistence, Serializable {

  /**
   * Gets the dao.
   *
   * @return the dao
   */
  public BibliographicModelItemDAO getDAO() {
    return new BibliographicModelItemDAO();
  }
}
