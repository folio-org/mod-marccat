/*
 * (c) LibriCore
 *
 * Created on Dec 3, 2004
 *
 * DAOBibItem.java
 */
package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import org.folio.marccat.dao.persistence.BIB_ITM;
import org.folio.marccat.dao.persistence.Cache;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.RecordNotFoundException;
import org.folio.marccat.exception.ReferentialIntegrityException;

import java.util.List;
import java.util.Objects;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/02/02 14:09:42 $
 * @since 1.0
 */
public class DAOBibItem extends AbstractDAO {
  @Deprecated
  //TODO
  public void delete(Persistence p) throws DataAccessException {
    if (!(p instanceof BIB_ITM)) {
      throw new IllegalArgumentException("Argument must be a BIB_ITM");
    }
    final BIB_ITM b = (BIB_ITM) p;
    // first check if other views exist for this bib
    List l = find(" select count(*) from BIB_ITM as b "
        + " where b.amicusNumber = ? and "
        + " b.userViewString <> ? ",
      new Object[]{b.getAmicusNumber(), b.getUserViewString()},
      new Type[]{Hibernate.INTEGER, Hibernate.STRING});

    if (((Integer) l.get(0)).intValue() == 0) {
      // if no other views then we can't have any holdings attached
      l = find(" select count(*) from SMRY_HLDG as sh where sh.bibItemNumber = ? ",
        new Object[]{b.getAmicusNumber()},
        new Type[]{Hibernate.INTEGER});
      if (((Integer) l.get(0)).intValue() > 0) {
        throw new ReferentialIntegrityException("SMRY_HLDG", "BIB_ITM");
      }
      // and we can't have any orders attached
      l = find(" select count(*) from ORDR_ITM_BIB_ITM as o where o.bibItemNumber = ? ",
        new Object[]{b.getAmicusNumber()},
        new Type[]{Hibernate.INTEGER});
      if (((Integer) l.get(0)).intValue() > 0) {
        throw new ReferentialIntegrityException("ORDR_ITM_BIT_ITM", "BIB_ITM");
      }
    }

    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s) throws HibernateException {
        s.delete(b);
        s.delete("from CLCTN_ACS_PNT as c " + " where c.bibItemNumber = ? ",
          new Object[]{b.getAmicusNumber()},
          new Type[]{Hibernate.INTEGER});
        s.delete("from " + Cache.class.getName() + " as c "
            + " where c.bibItemNumber = ? "
            + " and c.cataloguingView = ? ",
          new Object[]{b.getAmicusNumber(), new Short(View.toIntView(b.getUserViewString()))},
          new Type[]{Hibernate.INTEGER, Hibernate.SHORT});
      }
    }
      .execute();
  }

  /**
   * Gets the bibliographic record from BIB_ITM table.
   *
   * @param id       -- the amicus number of record.
   * @param userView -- the user view associated.
   * @param session  -- the current hibernate session.
   * @return BIB_ITM
   * @throws HibernateException -- in case of hibernate exception.
   */
  @SuppressWarnings("unchecked")
  public BIB_ITM load(final int id, final int userView, final Session session) throws HibernateException {
    List<BIB_ITM> l =
      session.find("from BIB_ITM as itm where itm.amicusNumber = ? "
          + " and SUBSTR(itm.userViewString, ?, 1) = '1'",
        new Object[]{id, userView},
        new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});


    if (l.stream().filter(Objects::nonNull).findFirst().isPresent()) {
      return (BIB_ITM) isolateView(l.stream().findFirst().get(), userView, session);
    } else {
      throw new RecordNotFoundException("BIB_ITM not found");
    }
  }
}
