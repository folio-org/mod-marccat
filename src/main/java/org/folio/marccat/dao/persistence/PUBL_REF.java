package org.folio.marccat.dao.persistence;

import org.folio.marccat.dao.DAODescriptor;
import org.folio.marccat.dao.PublisherDescriptorDAO;

/**
 * The different cross references for the publisher
 *
 * @author paulm
 * @author carment
 */
public class PUBL_REF extends REF {
  /** The DAO. */
  public DAODescriptor getTargetDAO() {
    return new PublisherDescriptorDAO();
  }

}
