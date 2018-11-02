/*
 * (c) LibriCore
 *
 * Created on Jul 27, 2004
 *
 * TTL_REF.java
 */
package org.folio.marccat.dao.persistence;

import org.folio.marccat.dao.DAODescriptor;
import org.folio.marccat.dao.TitleDescriptorDAO;

/**
 * @author paulm
 * @version %I%, %G%
 * @since 1.0
 */
public class TTL_REF extends REF {
  /* (non-Javadoc)
   * @see REF#getTargetDAO()
   */
  public DAODescriptor getTargetDAO() {
    return new TitleDescriptorDAO();
  }

}
