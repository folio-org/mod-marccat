
package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.exception.DataAccessException;
import java.sql.*;
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


  /**
   * Gets the preferred view for the view = 0 (Any)
   *
   * @param session the session
   * @param amicusNumber the amicus number
   * @param preferenceOrder the preference order
   * @return the preferred view
   */
  public int getPreferredView(final Session session, final int amicusNumber, final int preferenceOrder) {
    final AtomicInteger preferredView = new AtomicInteger();
    try (PreparedStatement stmt = getPreparedStatement(session.connection())) {
      stmt.setInt(1, amicusNumber);
      stmt.setInt(2, preferenceOrder);
      try(ResultSet resultSet = stmt.executeQuery()) {
        while (resultSet.next()) {
          preferredView.set(resultSet.getInt("trstn_vw_nbr"));
        }
      }
    } catch (SQLException | HibernateException e) {
      throw new DataAccessException(e);
    }
    return preferredView.get();
  }

  /**
   * return a PreparedStatement.
   *
   * @param connection the connection
   * @return the prepared statement
   * @throws SQLException the SQL exception
   */
  private PreparedStatement getPreparedStatement(final Connection connection) throws SQLException {
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
