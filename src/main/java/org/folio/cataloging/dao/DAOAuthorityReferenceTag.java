/*
 * (c) LibriCore
 *
 * Created on Dec 8, 2005
 *
 * DAOAuthorityReferenceTag.java
 */
package org.folio.cataloging.dao;

import org.folio.cataloging.business.cataloguing.authority.AuthorityReferenceTag;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.dao.persistence.REF;
import org.folio.cataloging.dao.persistence.ReferenceType;
import org.folio.cataloging.dao.persistence.T_DUAL_REF;

/**
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2006/01/11 13:36:22 $
 * @since 1.0
 */
public class DAOAuthorityReferenceTag extends AbstractDAO {

  /* (non-Javadoc)
   * @see HibernateUtil#delete(librisuite.business.common.Persistence)
   */
  public void delete(Persistence p) throws DataAccessException {
    AuthorityReferenceTag t = (AuthorityReferenceTag) p;
    REF ref = t.getReference ( );
    ref.getDAO ( ).delete (ref);

    /* we do not delete both sides of a dual
     * so comment out the following block */
//		if (t.isHasDualIndicator()) {
//			if (T_DUAL_REF.isDual(t.getDualReferenceIndicator())) {
//				REF dualRef = (REF)ref.clone();
//				dualRef.setType(ReferenceType.getReciprocal(ref.getType()));
//				dualRef.getDAO().delete(dualRef);
//			}
//		}
  }

  /* (non-Javadoc)
   * @see HibernateUtil#save(librisuite.business.common.Persistence)
   */
  public void save(Persistence p) throws DataAccessException {
    AuthorityReferenceTag t = (AuthorityReferenceTag) p;
    REF ref = t.getReference ( );
    ref.getDAO ( ).save (ref);
    if (t.isHasDualIndicator ( )) {
      if (T_DUAL_REF.isDual (t.getDualReferenceIndicator ( ))) {
        REF dualRef = (REF) ref.clone ( );
        dualRef.setType (ReferenceType.getReciprocal (ref.getType ( )));
        dualRef.getDAO ( ).save (dualRef);
      }
    }

  }

  /* (non-Javadoc)
   * @see HibernateUtil#update(librisuite.business.common.Persistence)
   */
  public void update(Persistence p) throws DataAccessException {
    delete (p);
    save (p);
  }

}
