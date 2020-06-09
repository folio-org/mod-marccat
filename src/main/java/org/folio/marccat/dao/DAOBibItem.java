package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.View;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.dao.persistence.BIB_ITM;
import org.folio.marccat.dao.persistence.Cache;
import org.folio.marccat.exception.RecordNotFoundException;
import java.util.List;
import java.util.Objects;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/02/02 14:09:42 $
 * @since 1.0
 */
public class DAOBibItem extends AbstractDAO {

  private static final Log logger = new Log(DAOBibItem.class);

  /**
   * Deletes persistent objects (bib_item, cache, full_cache) using hibernate transaction.
   *
   * @param p the persistent object.
   * @param session          the current hibernate session.
   * @throws HibernateException in case of hibernate exception.
   */
  @Override
  public void delete(final Persistence p, final Session session) throws HibernateException {
    if (!(p instanceof BIB_ITM)) {
      throw new IllegalArgumentException("Argument must be a BIB_ITM");
    }
    final BIB_ITM b = (BIB_ITM) p;
    session.delete(b);
    session.delete("from " + Cache.class.getName() + " as c "
        + " where c.bibItemNumber = ? "
        + " and c.cataloguingView = ? ",
      new Object[]{b.getAmicusNumber(), View.toIntView(b.getUserViewString())},
      new Type[]{Hibernate.INTEGER, Hibernate.SHORT});

    session.delete("from FULL_CACHE as c "
                + " where c.itemNumber = ? and c.userView = ? ",
      new Object[]{b.getAmicusNumber(), View.toIntView(b.getUserViewString())},
      new Type[]{Hibernate.INTEGER, Hibernate.SHORT});

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


    if (l.stream().anyMatch(Objects::nonNull)) {
      return (BIB_ITM) isolateView(l.stream().findFirst().get(), userView, session);
    } else {
      logger.debug("BIB_ITM not found");
      logger.info("The Exception dosn't block the insert flow");
      throw new RecordNotFoundException("BIB_ITM not found");
    }
  }
}
