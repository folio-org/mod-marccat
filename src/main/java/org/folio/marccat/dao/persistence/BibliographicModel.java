package org.folio.marccat.dao.persistence;

import org.folio.marccat.dao.AbstractDAO;

/**
 * Represents a Model/Template for of bibliographic type
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public class BibliographicModel extends Model {

  /**
   * Class constructor.
   *
   * @since 1.0
   */
  public BibliographicModel() {
    super();
  }

  /**
   * Gets the dao.
   *
   * @deprecated  Not for public use.
   * @return the dao
   */
  @Deprecated
  public AbstractDAO getDAO() {
    return null;
  }
}
