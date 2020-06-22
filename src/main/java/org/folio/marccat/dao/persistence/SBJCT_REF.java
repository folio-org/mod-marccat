package org.folio.marccat.dao.persistence;

import org.folio.marccat.dao.DescriptorDAO;
import org.folio.marccat.dao.SubjectDescriptorDAO;

/**
 * The different cross references for the subject
 *
 * @author paulm
 * @author carment
 */
public class SBJCT_REF extends REF {
  /**
   * The DAO.
   */
  public DescriptorDAO getTargetDAO() {
    return new SubjectDescriptorDAO();
  }

}
