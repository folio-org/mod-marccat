/*
 * (c) LibriCore
 *
 * Created on Jan 24, 2005
 *
 * DAOInventory.java
 */
package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import org.folio.marccat.dao.persistence.Inventory;
import org.folio.marccat.dao.persistence.S_INVTRY;
import org.folio.marccat.exception.DataAccessException;

import java.util.List;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/02/02 14:09:42 $
 * @since 1.0
 */
public class DAOInventory extends AbstractDAO {

  public int getNextNumber(final int mainLibrary)
    throws DataAccessException {
    final S_INVTRY nextNumber =
      (S_INVTRY) get(S_INVTRY.class,
        (mainLibrary),
        LockMode.UPGRADE);
    int j = nextNumber.getNextNumber();
    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s)
        throws HibernateException {
        int i = nextNumber.getNextNumber() + 1;
        nextNumber.setNextNumber(i);
        s.update(nextNumber);
      }
    }
      .execute();

    return /*nextNumber.getNextNumber()*/j;
  }


}
