package org.folio.marccat.dao;

import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.dao.persistence.AuthorityReferenceTag;
import org.folio.marccat.dao.persistence.REF;
import org.folio.marccat.dao.persistence.ReferenceType;
import org.folio.marccat.dao.persistence.T_DUAL_REF;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

/**
 * @author elena
 *
 */
public class AuthorityReferenceTagDAO extends AbstractDAO {

  @Override
  public void save(Persistence p, final Session session) throws HibernateException {
    AuthorityReferenceTag t = (AuthorityReferenceTag) p;
    REF ref = t.getReference();
    ref.getDAO().save(ref, session);
    if (t.isHasDualIndicator() && T_DUAL_REF.isDual(t.getDualReferenceIndicator())) {
      REF dualRef = (REF) ref.copy();
      dualRef.setType(ReferenceType.getReciprocal(ref.getType()));
      dualRef.getDAO().save(dualRef, session);
    }
  }

}
