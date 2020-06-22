package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.search.SearchResponse;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Class SortResultSetsDAO.
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public class SortResultSetsDAO extends AbstractDAO {

  /**
   * Sort.
   *
   * @param session the session
   * @param rs the rs
   * @param attributes the attributes
   * @param directions the directions
   */
  public void sort(
    final Session session,
    final SearchResponse rs,
    String[] attributes,
    String[] directions) throws DataAccessException {
    final String orderBy = buildOrderByClause(attributes, directions);
    /*
     * We use a transaction here to ensure that a commit is NOT done after the
     * insert (since this would delete the rows just inserted).  And also to ensure
     * that the inserted rows are removed before the next sort
     */
    Transaction tx = null;
    try {
      tx = getTransaction(session);
      insertResults(rs, session);
      doSort(orderBy, rs, session);
      tx.commit();
    } catch (HibernateException exception) {
      cleanUp(tx);
      throw new DataAccessException(exception);
    }
  }

  /**
   * Do sort.
   *
   * @param orderBy the order by
   * @param rs the rs
   * @param session the session
   */
  private void doSort(final String orderBy, final SearchResponse rs,  final Session session) {
    String query = "select bib_itm_nbr "
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
      + ")";
    try (PreparedStatement stmt = session.connection().prepareStatement(query);
         ResultSet js = stmt.executeQuery()) {
      while (js.next()) {
        rs.getIdSet()[js.getRow() - 1] = js.getInt(1);
      }
    } catch (SQLException | HibernateException e) {
      throw new DataAccessException(e);
    }
  }


  /**
   * Insert results.
   *
   * @param rs the rs
   * @param session the session
   */
  private void insertResults(final SearchResponse rs, final Session session) {
    try (PreparedStatement stmt = session.connection().prepareStatement("INSERT INTO S_SRCH_SRT_RSLTS VALUES(?)")){
      for (int i = 0; i < rs.getIdSet().length; i++) {
        stmt.setInt(1, rs.getIdSet()[i]);
        stmt.addBatch();
      }
      stmt.executeBatch();
    } catch (SQLException | HibernateException e) {
      throw new DataAccessException(e);
    }
  }


  /**
   * Builds the order by clause.
   *
   * @param attributes the attributes
   * @param directions the directions
   * @return the string
   */
  /*
   * convert z3950 use attributes to column numbers from cache table and build
   * an sql order by clause for the sort
   */
  private String buildOrderByClause(
    String[] attributes,
    String[] directions) {
    StringBuilder buf = new StringBuilder();
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
