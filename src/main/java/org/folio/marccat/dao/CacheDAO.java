
package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import org.folio.marccat.dao.persistence.Cache;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.RecordNotFoundException;
import java.sql.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Provides data access to S_CACHE_BIB_ITM_DSPLY table
 *
 * @author paulm
 * @author natasciab
 * @author carment
 * @since 1.0
 */
public class CacheDAO extends AbstractDAO {


  public Cache load(final int bibItemNumber, final int cataloguingView,final Session session) throws HibernateException {
    List<Cache> l =
     session.find(
        "from Cache as c "
          + " where c.bibItemNumber = ? and c.cataloguingView = ?",
        new Object[]{
          (bibItemNumber),
          (cataloguingView)},
        new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});
    if (l.isEmpty()) {
      throw new RecordNotFoundException("Cache entry not found");
    }
    return  l.get(0);
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
        try (final PreparedStatement stmt = stmt(connection);
             final ResultSet resultSet = stmt.executeQuery()) {
          stmt.setInt(1, amicusNumber);
          stmt.setInt(2, preferenceOrder);
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

  private PreparedStatement stmt(final Connection connection) throws SQLException {
    return connection.prepareStatement(
      "SELECT a1.trstn_vw_nbr FROM (" +
        "SELECT b.trstn_vw_nbr " +
        " FROM s_cache_bib_itm_dsply a, " +
        " db_prfr_ordr_seq b " +
        " WHERE bib_itm_nbr = ? and " +
        " a.trstn_vw_nbr = b.trstn_vw_nbr and " +
        " b.DB_PRFNC_ORDR_NBR = ? " +
        " order by b.vw_seq_nbr) a1" +
        " limit 2");

  }
}
