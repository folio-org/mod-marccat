package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.dao.common.HibernateUtil;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.search.SearchResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/07/11 08:01:05 $
 * @since 1.0
 */
public class DAOSortResultSets extends HibernateUtil {

  public void sort(
    final Session session,
    final SearchResponse rs,
    String[] attributes,
    String[] directions)
    throws DataAccessException {
    final String orderBy = buildOrderByClause(attributes, directions);
    /*
     * We use a transaction here to ensure that a commit is NOT done after the
     * insert (since this would delete the rows just inserted).  And also to ensure
     * that the inserted rows are removed before the next sort
     */
    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s)
        throws DataAccessException {
        SearchResponse sortedResults;
        insertResults(rs);
        doSort(orderBy, rs);
      }
    }
      .execute(session);
  }

  private void doSort(
    final String orderBy,
    final SearchResponse rs)
    throws DataAccessException {

    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s)
        throws HibernateException, DataAccessException {
        Connection connection = s.connection();
        PreparedStatement stmt = null;
        java.sql.ResultSet js = null;
        try {

          stmt =
            connection.prepareStatement(
              "select bib_itm_nbr "
                + "from (select bib_itm_nbr, TTL_HDG_MAIN_SRT_FORM, "
                + "MAIN_ENTRY_SRT_FORM, "
                + "BIB_NTE_IPRNT_STRNG_TXT, "
                + "LANG_OF_CTLGG_CDE, "
                + "ITM_DTE_1_DSC, "
                + "ITM_REC_TYP_CDE, TTL_HDG_SRS_STRNG_TXT, TTL_VOL_NBR_1_DSC  "
                + "from s_cache_bib_itm_dsply"
                + ",s_srch_srt_rslts "
                + "where bib_itm_nbr = itm_nbr and "
                + "trstn_vw_nbr = "
                + rs.getSearchingView() + " "
                + orderBy
                + ")");

          js = stmt.executeQuery();

          while (js.next()) {
            rs.getIdSet()[js.getRow() - 1] = js.getInt(1);
          }
        } catch (SQLException e) {
          throw new DataAccessException();
        } finally {
          if (js != null) {
            try {
              js.close();
            } catch (SQLException e) {
            }
          }
          if (stmt != null) {
            try {
              stmt.close();
            } catch (SQLException e) {
            }
          }
        }
      }
    }
      .execute();
  }


  private void insertResults(final SearchResponse rs)
    throws DataAccessException {
    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s)
        throws HibernateException, DataAccessException {
        Connection connection = s.connection();
        PreparedStatement stmt = null;
        int[] iNoRows = null;

        try {
          stmt = connection.prepareStatement(
            "INSERT INTO S_SRCH_SRT_RSLTS VALUES(?)");
          for (int i = 0; i < rs.getIdSet().length; i++) {
            stmt.setInt(1, rs.getIdSet()[i]);
            stmt.addBatch();
          }
          iNoRows = stmt.executeBatch();
        } catch (SQLException e) {
          throw new DataAccessException();
        } finally {
          if (stmt != null) {
            try {
              stmt.close();
            } catch (SQLException e) {
            }
          }
        }
      }
    }
      .execute();
  }


  /*
   * convert z3950 use attributes to column numbers from cache table and build
   * an sql order by clause for the sort
   */
  private String buildOrderByClause(
    String[] attributes,
    String[] directions) {
    StringBuffer buf = new StringBuffer();
    int column;
    buf.append(" ORDER BY ");
    for (int i = 0; i < attributes.length; i++) {
      switch (Integer.parseInt(attributes[i])) {
        case 4:
          column = 2;
          break;
        case 1003:
          column = 3;
          break;
        case 1018:
        case 59:
          column = 4;
          break;
        case 54:
          column = 5;
          break;
        case 31:
          column = 6;
          break;
        case 1001:
          column = 7;
          break;
        case 2255:
          column = 8;
          break;
        default:
          column = 2; // bad attributes ==> title sort
          break;
      }

      if (column == 8)
        buf.append(" UPPER(TTL_HDG_SRS_STRNG_TXT),TTL_VOL_NBR_1_DSC ");
      else
        buf.append(column + " ");
      buf.append(directions[i].equals("0") ? "asc" : "desc");
      buf.append(", ");
    }
    buf.deleteCharAt(buf.lastIndexOf(",")); // remove trailing comma
    return buf.toString();
  }
}
