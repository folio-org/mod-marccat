/*
 * (c) LibriCore
 *
 * Created on Dec 22, 2004
 *
 * DAOPublisherTag.java
 */
package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.cataloguing.bibliographic.PublisherTag;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.dao.common.HibernateUtil;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import org.folio.marccat.dao.persistence.PublisherAccessPoint;

import java.util.Iterator;

/**
 * Although PublisherTag implements Persistence, it is in fact not mapped to a table
 * through Hibernate.  Instead it delegates persistence to its constituent access points
 *
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2005/12/21 08:30:32 $
 * @since 1.0
 */
public class DAOPublisherTag extends HibernateUtil {

  /* (non-Javadoc)
   * @see HibernateUtil#delete(librisuite.business.common.Persistence)
   */
  @Override
  public void delete(Persistence po) {
    if (!(po instanceof PublisherTag)) {
      throw new IllegalArgumentException("I can only persist PublisherTag objects");
    }
    PublisherTag aPub = (PublisherTag) po;
    Iterator iter = aPub.getAccessPoints().iterator();
    PublisherAccessPoint apf;
    while (iter.hasNext()) {
      apf = (PublisherAccessPoint) iter.next();
      apf.markDeleted();
      super.delete(apf);
    }
  }

  /* (non-Javadoc)
   * @see HibernateUtil#save(librisuite.business.common.Persistence)
   */
  @Override
  public void save(final Persistence po) {
    if (!(po instanceof PublisherTag)) {
      throw new IllegalArgumentException("I can only persist PublisherTag objects");
    }
    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s)
        throws HibernateException {
        PublisherTag aPub = (PublisherTag) po;
        /*
         * The approach taken to saving publisher tags is to first delete all existing
         * apfs for this tag and then to add back the new ones.
         */
        s.delete(
          "from PublisherAccessPoint as apf "
            + " where apf.bibItemNumber = ? and "
            + " apf.userViewString = ? ",
          new Object[]{
            (aPub.getItemNumber()),
            aPub.getUserViewString()},
          new Type[]{Hibernate.INTEGER, Hibernate.STRING});

        Iterator iter = aPub.getAccessPoints().iterator();
        PublisherAccessPoint apf;
        while (iter.hasNext()) {
          apf = (PublisherAccessPoint) iter.next();
          apf.setBibItemNumber(aPub.getItemNumber());
          apf.setUserViewString(aPub.getUserViewString());
          apf.markNew();
        }
      }
    }
      .execute();
  }

  /* (non-Javadoc)
   * @see HibernateUtil#update(librisuite.business.common.Persistence)
   */
  @Override
  public void update(Persistence p) {
    /*
     * Since we are deleting and re-adding, save and update are the same
     */
    save(p);
  }

}
