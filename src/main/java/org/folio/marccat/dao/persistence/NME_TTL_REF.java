package org.folio.marccat.dao.persistence;

import org.folio.marccat.dao.DAODescriptor;
import org.folio.marccat.dao.NameTitleDescriptorDAO;

/**
 * The different cross references for the titles/name
 *
 * @author paulm
 * @author carment
 */
public class NME_TTL_REF extends REF {
  /** The DAO. */
  public DAODescriptor getTargetDAO() {
    return new NameTitleDescriptorDAO();
  }

}
