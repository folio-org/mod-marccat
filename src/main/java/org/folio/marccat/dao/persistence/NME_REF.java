package org.folio.marccat.dao.persistence;

import org.folio.marccat.dao.DAODescriptor;
import org.folio.marccat.dao.NameDescriptorDAO;

/**
 * The different cross references for the name
 *
 * @author paulm
 * @author carment
 */
public class NME_REF extends REF {
  /** The DAO. */
  public DAODescriptor getTargetDAO() {
    return new NameDescriptorDAO();
  }

}
