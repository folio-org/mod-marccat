/*
 * (c) LibriCore
 *
 * Created on Jun 22, 2004
 *
 * SBJCT_REF.java
 */
package org.folio.marccat.dao.persistence;

import org.folio.marccat.dao.DAODescriptor;
import org.folio.marccat.dao.DAOThesaurusDescriptor;

/**
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/21 08:30:33 $
 * @since 1.0
 */
public class THS_ATRIB extends REF {
  /* (non-Javadoc)
   * @see REF#getTargetDAO()
   */
  public DAODescriptor getTargetDAO() {
    return new DAOThesaurusDescriptor();
  }

}
