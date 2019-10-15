package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.dao.common.HibernateUtil;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import org.folio.marccat.dao.persistence.Cache;
import org.folio.marccat.exception.CacheUpdateException;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.RecordNotFoundException;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/02/02 14:09:42 $
 * @since 1.0
 */
@Component
public class DAOCache extends AbstractDAO {


  public Cache load(int bibItemNumber, int cataloguingView) {
    List l =
      find(
        "from Cache as c "
          + " where c.bibItemNumber = ? and c.cataloguingView = ?",
        new Object[]{
          (bibItemNumber),
          (cataloguingView)},
        new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});
    if (l.isEmpty()) {
      throw new RecordNotFoundException("Cache entry not found");
    }
    return (Cache) l.get(0);
  }




  /**
   * Determines the correct view to retrieve for the given amicusNumber
   * based on the contents of the cache and the user's selected preference
   * order
   *
   * @param amicusNumber
   * @param preferenceOrder
   * @return
   * @throws DataAccessException
   */
  public int getPreferredView(final Session session, final int amicusNumber, final int preferenceOrder) {
    final AtomicInteger preferredView = new AtomicInteger();
    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(final Session s) throws HibernateException {
        final Connection connection = s.connection();
        try (final PreparedStatement stmt = stmt(connection, amicusNumber, preferenceOrder);
             final ResultSet resultSet = stmt.executeQuery()) {
          while (resultSet.next()) {
            preferredView.set(resultSet.getInt("trstn_vw_nbr"));
          }
        } catch (final SQLException exception) {
          throw new DataAccessException(exception);
        }
      }
    }.execute(session);
    return preferredView.get();
  }

  private PreparedStatement stmt(final Connection connection, final int recordId, final int preferenceOrder) throws SQLException {
    final PreparedStatement stmt = connection.prepareStatement(
      "SELECT a1.trstn_vw_nbr FROM (" +
        "SELECT b.trstn_vw_nbr " +
        " FROM s_cache_bib_itm_dsply a, " +
        " db_prfr_ordr_seq b " +
        " WHERE bib_itm_nbr = ? and " +
        " a.trstn_vw_nbr = b.trstn_vw_nbr and " +
        " b.DB_PRFNC_ORDR_NBR = ? " +
        " order by b.vw_seq_nbr) a1" +
        " limit 2");
    stmt.setInt(1, recordId);
    stmt.setInt(2, preferenceOrder);
    return stmt;
  }
}
