/*
 * (c) LibriCore
 *
 * Created on Dec 15, 2004
 *
 * PUBL_REF.java
 */
package org.folio.marccat.dao.persistence;

import org.folio.marccat.dao.DAODescriptor;
import org.folio.marccat.dao.PublisherDescriptorDAO;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/21 08:30:33 $
 * @since 1.0
 */
public class PUBL_REF extends REF {
  /* (non-Javadoc)
   * @see REF#getTargetDAO()
   */
  public DAODescriptor getTargetDAO() {
    return new PublisherDescriptorDAO();
  }

}
