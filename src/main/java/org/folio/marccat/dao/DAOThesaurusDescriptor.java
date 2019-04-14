/*
 * (c) LibriCore
 *
 * Created on Dec 3, 2004
 *
 * DAOThesaurusDescriptor.java
 */
package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.THS_HDG;
import org.folio.marccat.exception.ReferentialIntegrityException;

import java.util.List;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/21 08:30:32 $
 * @since 1.0
 */
public class DAOThesaurusDescriptor extends DAODescriptor {

  protected static Class persistentClass = THS_HDG.class;

  /* (non-Javadoc)
   * @see com.libricore.librisuite.business.Descriptor#getPersistentClass()
   */
  public Class getPersistentClass() {
    return DAOThesaurusDescriptor.persistentClass;
  }

  @Override
  public boolean supportsAuthorities() {
    return true;
  }

  public List getHeadingsBySortform(String operator, String direction, String term, String filter, int cataloguingView, int count) {
    Session s = currentSession();
    List l = null;
    try {
      String quetyString = "select distinct ths_hdg from "
        + getPersistentClass().getName()
        + " as ths_hdg where ths_hdg.sortForm "
        + operator
        + " :term  and "
        + " SUBSTR(ths_hdg.key.userViewString, :view, 1) = '1' "
        + filter
        + " order by ths_hdg.sortForm "
        + direction;

      Query q =
        s.createQuery(
          quetyString);
      q.setString("term", term);
      q.setInteger("view", cataloguingView);
      q.setMaxResults(count);
      l = q.list();

    } catch (HibernateException e) {
      logAndWrap(e);
    }

    return l;
  }

  /*Questa heading ha solo cross reference*/
  @Override
  public void delete(Persistence p) {
    if (!(p instanceof Descriptor)) {
      throw new IllegalArgumentException("I can only delete Descriptor objects");
    }
    Descriptor d = ((Descriptor) p);
    // check for authorities
    if (supportsAuthorities() && d.getAuthorityCount() > 0) {
      throw new ReferentialIntegrityException(
        d.getReferenceClass(d.getClass()).getName(),
        d.getClass().getName());
    }
    // OK, go ahead and delete
    deleteDescriptor(p);
  }

  /**
   * Default implementation for delete with no cascade affects
   *
   * @since 1.0
   */
  public void deleteDescriptor(final Persistence p) {
    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s)
        throws HibernateException {
        s.delete(p);
      }
    }
      .execute();
  }


}
