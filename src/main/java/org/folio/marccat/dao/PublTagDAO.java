package org.folio.marccat.dao;

import org.folio.marccat.dao.persistence.PUBL_TAG;

/**
 * Data access of the pub_tag table
 *
 * @author paulm
 * @author natasciab
 * @since 1.0
 */

public class PublTagDAO extends DescriptorDAO {
  /**
   * Gets the persistent class.
   *
   * @return the persistent class
   */
  public Class getPersistentClass() {
    return PUBL_TAG.class;
  }

}
