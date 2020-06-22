package org.folio.marccat.dao;

import net.sf.hibernate.Session;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.dao.persistence.S_BIB1_SMNTC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Semantic Data access object.
 *
 * @author paulm
 * @since 1.0
 */
public class SemanticDAO {
  private static final Log logger = new Log(SemanticDAO.class);

  /**
   * Gets the semantic entry for java search engine.
   *
   * @param session the session
   * @param useNumber the use number
   * @param relationNumber the relation number
   * @param positionNumber the position number
   * @param structureNumber the structure number
   * @param truncationNumber the truncation number
   * @param completenessNumber the completeness number
   * @param recordTypeCode the record type code
   * @return the semantic entry
   * @throws Exception the exception
   */
  public S_BIB1_SMNTC getSemanticEntry(
    final Session session,
    int useNumber,
    int relationNumber,
    int positionNumber,
    int structureNumber,
    int truncationNumber,
    int completenessNumber,
    short recordTypeCode) throws Exception {
    logger.debug("get(" + useNumber + "," + relationNumber + "," + positionNumber + "," +
      structureNumber + "," + truncationNumber + "," + completenessNumber + "," +
      recordTypeCode + ")");

    Connection connection = null;
    PreparedStatement selectStatement = null;
    ResultSet rs = null;

    try {
      connection = session.connection();
      selectStatement = connection.prepareStatement("select * from s_bib1_smntc " +
        " where atrbt_use_nbr = ? and atrbt_rltn_nbr = ? and " +
        " atrbt_pstn_nbr = ? and atrbt_strct_nbr = ? and " +
        " atrbt_trntn_nbr = ? and atrbt_cmpns_nbr = ? and db_rec_typ_cde = ?");
      int i = 1;
      selectStatement.setInt(i++, useNumber);
      selectStatement.setInt(i++, relationNumber);
      selectStatement.setInt(i++, positionNumber);
      selectStatement.setInt(i++, structureNumber);
      selectStatement.setInt(i++, truncationNumber);
      selectStatement.setInt(i++, completenessNumber);
      selectStatement.setShort(i++, recordTypeCode);
      rs = selectStatement.executeQuery();
      if (rs.next()) {
        S_BIB1_SMNTC result = new S_BIB1_SMNTC();
        result.setUseNumber(rs.getInt("atrbt_use_nbr"));
        result.setRelationNumber(rs.getInt("atrbt_rltn_nbr"));
        result.setPositionNumber(rs.getInt("atrbt_pstn_nbr"));
        result.setStructureNumber(rs.getInt("atrbt_strct_nbr"));
        result.setTruncationNumber(rs.getInt("atrbt_trntn_nbr"));
        result.setCompletenessNumber(rs.getInt("atrbt_cmpns_nbr"));
        result.setRecordTypeCode(rs.getShort("db_rec_typ_cde"));
        result.setSortFormSkipInFilingCode(rs.getShort("srt_form_skp_in_flng_cde"));
        result.setSortFormFunctionCode(rs.getShort("srt_form_fnctn_cde"));
        result.setSortFormTypeCode(rs.getShort("srt_form_typ_cde"));
        result.setSortFormSubTypeCode(rs.getShort("srt_form_sub_typ_cde"));
        result.setSortFormMainTypeCode(rs.getShort("srt_form_main_typ_cde"));
        result.setQueryActionCode(rs.getString("qry_actn_cde"));
        result.setSecondaryIndexCode(rs.getByte("scdry_idx_cde"));
        result.setSelectClause(rs.getString("sql_slct"));
        logger.debug("select is '" + result.getSelectClause() + "'");
        result.setFromClause(rs.getString("sql_frm"));
        result.setWhereClause(rs.getString("sql_whr"));
        result.setJoinClause(rs.getString("sql_jn"));
        result.setViewClause(rs.getString("sql_vw"));
        result.setFullText(rs.getBoolean("context_idx_cde"));
        return result;
      } else {
        return null;
      }
    } finally {
      try {
        if(rs != null)
          rs.close();
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
      }
      try {
        if(selectStatement != null)
          selectStatement.close();
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
      }
    }
  }
}
