/*
 * (c) LibriCore
 *
 * Created on 12 Jun 2007
 *
 * DAOPredictionPattern.java
 */
package org.folio.cataloging.business.patterns;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DAOPredictionPattern {
  private static final Log logger = LogFactory.getLog (DAOPredictionPattern.class);

  public PredictionPattern load(int predictionPatternNumber)
    throws SQLException {
    Connection conn = ConnectionFactory.getConnection ( );

    PredictionPattern result = new PredictionPattern ( );

    PreparedStatement stmt = conn
      .prepareStatement ("SELECT * FROM SRL_PRED_PAT WHERE PRD_PAT_NBR = ?");
    stmt.setInt (1, predictionPatternNumber);
    ResultSet rs = stmt.executeQuery ( );
    while (rs.next ( )) {
      result.setPredictionPatternNumber (predictionPatternNumber);
      result.setLabel (rs.getString ("label"));
      result.setCaptionFormat (rs.getString ("caption"));
      result
        .setPublicationDetails (loadPublicationDetails (predictionPatternNumber));
      result
        .setCalendarChanges (loadCalendarChanges (predictionPatternNumber));
      result
        .setCombinedPublications (loadCombinedPublications (predictionPatternNumber));
      result.setIndexes (loadIndexes (predictionPatternNumber));
      result
        .setSpecialPublications (loadSpecialPublications (predictionPatternNumber));
      result
        .setEnumerationPattern (loadEnumerationPattern (predictionPatternNumber));
    }
    rs.close ( );
    stmt.close ( );
    return result;
  }

  public void saveIssues(Map enumMap) throws SQLException {
    Connection conn = ConnectionFactory.getConnection ( );
    PreparedStatement stmt = conn
      .prepareStatement ("INSERT INTO S_WRK_PRED_PAT (PUB_DTE, TYPE, CAPTION) VALUES(?, 1, ?)");
    Iterator iter = enumMap.keySet ( ).iterator ( );
    while (iter.hasNext ( )) {
      Date aDate = (Date) iter.next ( );
      stmt.setDate (1, new java.sql.Date (aDate.getTime ( )));
      stmt.setString (2, (String) enumMap.get (aDate));
      stmt.addBatch ( );
    }
    stmt.executeBatch ( );
    stmt.close ( );
  }

  public void saveIndexes(Set dates) throws SQLException {
    Connection conn = ConnectionFactory.getConnection ( );
    PreparedStatement stmt = conn
      .prepareStatement ("INSERT INTO S_WRK_PRED_PAT (PUB_DTE, TYPE) VALUES(?, 3)");
    Iterator iter = dates.iterator ( );
    while (iter.hasNext ( )) {
      Date aDate = (Date) iter.next ( );
      stmt.setDate (1, new java.sql.Date (aDate.getTime ( )));
      stmt.addBatch ( );
      logger.debug ("adding " + aDate.getTime ( ) + " to s_wrk_pred_pat");
    }
    stmt.executeBatch ( );
    stmt.close ( );
  }

  public void saveSpecial(Set dates) throws SQLException {
    Connection conn = ConnectionFactory.getConnection ( );
    PreparedStatement stmt = conn
      .prepareStatement ("INSERT INTO S_WRK_PRED_PAT (PUB_DTE, TYPE) VALUES(?, 4)");
    Iterator iter = dates.iterator ( );
    while (iter.hasNext ( )) {
      Date aDate = (Date) iter.next ( );
      stmt.setDate (1, new java.sql.Date (aDate.getTime ( )));
      stmt.addBatch ( );
    }
    stmt.executeBatch ( );
    stmt.close ( );
  }

  private List loadPublicationDetails(int predictionPatternNumber)
    throws SQLException {
    return loadDetailsByType (1, predictionPatternNumber);
  }

  private List loadCalendarChanges(int predictionPatternNumber)
    throws SQLException {
    return loadDetailsByType (2, predictionPatternNumber);
  }

  private List loadIndexes(int predictionPatternNumber) throws SQLException {
    return loadDetailsByType (3, predictionPatternNumber);
  }

  private List loadSpecialPublications(int predictionPatternNumber)
    throws SQLException {
    return loadDetailsByType (4, predictionPatternNumber);
  }

  private List loadCombinedPublications(int predictionPatternNumber)
    throws SQLException {
    return loadDetailsByType (5, predictionPatternNumber);
  }

  private List loadDetailsByType(int type, int predictionPatternNumber)
    throws SQLException {
    List result = new ArrayList ( );
    PredictionPatternDetail aDetail;
    Connection conn = ConnectionFactory.getConnection ( );
    PreparedStatement stmt = conn
      .prepareStatement ("SELECT * FROM SRL_PRED_PAT_DTL WHERE PRD_PAT_NBR = ? AND "
        + "TYPE = ? ORDER BY SEQ_NBR");
    stmt.setInt (1, predictionPatternNumber);
    stmt.setInt (2, type);
    ResultSet rs = stmt.executeQuery ( );
    while (rs.next ( )) {
      aDetail = new PredictionPatternDetail ( );
      aDetail.setPredictionPatternNumber (predictionPatternNumber);
      aDetail.setSequenceNumber (rs.getInt ("seq_nbr"));
      aDetail.setDay (rs.getInt ("day"));
      aDetail.setMonth (rs.getInt ("month"));
      aDetail.setOrdinal (rs.getInt ("ordinal"));
      aDetail.setPatternClass (rs.getInt ("class"));
      aDetail.setStepCount (rs.getInt ("step_count"));
      aDetail.setTimeField (rs.getInt ("time_field"));
      List daysOfWeek = new ArrayList ( );
      if (rs.getInt ("Sunday") > 0) {
        daysOfWeek.add (new Integer (1));
      }
      if (rs.getInt ("Monday") > 0) {
        daysOfWeek.add (new Integer (2));
      }
      if (rs.getInt ("Tuesday") > 0) {
        daysOfWeek.add (new Integer (3));
      }
      if (rs.getInt ("Wednesday") > 0) {
        daysOfWeek.add (new Integer (4));
      }
      if (rs.getInt ("Thursday") > 0) {
        daysOfWeek.add (new Integer (5));
      }
      if (rs.getInt ("Friday") > 0) {
        daysOfWeek.add (new Integer (6));
      }
      if (rs.getInt ("Saturday") > 0) {
        daysOfWeek.add (new Integer (7));
      }
      aDetail.setDaysOfWeek (daysOfWeek);
      result.add (aDetail);
    }
    rs.close ( );
    stmt.close ( );
    return result;
  }

  private List loadEnumerationPattern(int predictionPatternNumber)
    throws SQLException {
    List result = new ArrayList ( );
    EnumPattern aDetail;
    Connection conn = ConnectionFactory.getConnection ( );
    PreparedStatement stmt = conn
      .prepareStatement ("SELECT * FROM SRL_ENUM WHERE PRD_PAT_NBR = ? ORDER BY SEQ_NBR");
    stmt.setInt (1, predictionPatternNumber);
    ResultSet rs = stmt.executeQuery ( );
    while (rs.next ( )) {
      aDetail = new EnumPattern ( );
      aDetail.setPredictionPatternNumber (predictionPatternNumber);
      aDetail.setSequenceNumber (rs.getInt ("seq_nbr"));
      aDetail
        .setNumberingContinuous (rs.getString ("nbr_cont")
          .equals ("c"));
      aDetail.setNumberOfUnits (rs.getInt ("nbr_bib_units"));
      aDetail.setStartAt (rs.getInt ("start_at"));
      aDetail.setStartAtDate (rs.getDate ("pub_dte"));
      result.add (aDetail);
    }
    rs.close ( );
    stmt.close ( );
    return result;
  }
}
