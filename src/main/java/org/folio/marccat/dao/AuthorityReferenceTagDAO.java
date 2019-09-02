/*
 * (c) LibriCore
 *
 * Created on Dec 8, 2005
 *
 * DAOAuthorityReferenceTag.java
 */
package org.folio.marccat.dao;

import org.folio.marccat.business.cataloguing.authority.AuthorityReferenceTag;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.dao.persistence.REF;
import org.folio.marccat.dao.persistence.ReferenceType;
import org.folio.marccat.dao.persistence.T_DUAL_REF;

/**
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2006/01/11 13:36:22 $
 * @since 1.0
 */
public class AuthorityReferenceTagDAO extends AbstractDAO {

  /* (non-Javadoc)
   * @see HibernateUtil#delete(librisuite.business.common.Persistence)
   */
  @Override
  public void delete(Persistence p) {
    AuthorityReferenceTag t = (AuthorityReferenceTag) p;
    REF ref = t.getReference();
    ref.getDAO().delete(ref);

    /* we do not delete both sides of a dual
     * so comment out the following block */
  }

  /* (non-Javadoc)
   * @see HibernateUtil#save(librisuite.business.common.Persistence)
   */
  @Override
  public void save(Persistence p) {
    AuthorityReferenceTag t = (AuthorityReferenceTag) p;
    REF ref = t.getReference();
    ref.getDAO().save(ref);
    if (t.isHasDualIndicator() && T_DUAL_REF.isDual(t.getDualReferenceIndicator())) {
      REF dualRef = (REF) ref.clone();
      dualRef.setType(ReferenceType.getReciprocal(ref.getType()));
      dualRef.getDAO().save(dualRef);
    }
  }

  /* (non-Javadoc)
   * @see HibernateUtil#update(librisuite.business.common.Persistence)
   */
  @Override
  public void update(Persistence p) {
    delete(p);
    save(p);
  }

}
